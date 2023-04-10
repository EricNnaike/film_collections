package com.example.filmcollection.service;

import com.example.filmcollection.dto.LoginDTO;
import com.example.filmcollection.dto.RateDTO;
import com.example.filmcollection.dto.UserDTO;
import com.example.filmcollection.entity.*;
import com.example.filmcollection.exceptions.CustomNotFoundException;
import com.example.filmcollection.repository.*;
import com.example.filmcollection.response.*;
import jakarta.transaction.Transactional;
import kotlin.collections.ArrayDeque;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private RestTemplate restTemplate;
    private final FilmsRepository filmsRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public UserServiceImpl(FilmsRepository filmsRepository, UserRepository userRepository, RatingRepository ratingRepository) {
        this.filmsRepository = filmsRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Users create(UserDTO userDTO) {
        Optional<Users> user = userRepository.findUsersByEmail(userDTO.getEmail());
        if (user.isPresent()) {
            throw new CustomNotFoundException("Email taken");
        }

        Users users = new Users();
        users.setEmail(userDTO.getEmail());
        users.setName(userDTO.getName());
        users.setPassword(userDTO.getPassword());

        userRepository.save(users);
        return users;
    }

    @Override
    public LoginResponse login(LoginDTO loginDTO) {

        Optional<Users> user = userRepository.findUsersByEmail(loginDTO.getEmail());
        if (user.isEmpty()) {
            throw new CustomNotFoundException("Invalid Email Address");
        }
        if (user.get().getPassword().equals(loginDTO.getPassword())){
//            String token = jwtService.generateJwtToken(user.get().getId());
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setId(user.get().getId());
            loginResponse.setEmail(user.get().getEmail());
           loginResponse.setMessage("Success");

            return loginResponse;
        }  return new LoginResponse("Invalid Password");
    }

    @Override
    public List<FilmsResponse> retrieveAndSaveMovies() {
        String apiKey = "4ce6b6fb8ec3a66ee18a06841a7184e4";
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=4ce6b6fb8ec3a66ee18a06841a7184e4&page=50\n" +
                "\n" + apiKey;

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String response = responseEntity.getBody();

        JSONObject jsonObject = new JSONObject(response);
        JSONArray results = jsonObject.getJSONArray("results");

        System.out.printf("Reesult "+ results);

        List<FilmsResponse> responseList = new ArrayDeque<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject result = results.getJSONObject(i);

            long id = result.getLong("id");
            String title = result.getString("title");
            String releaseDate = result.getString("release_date");
            String overview = result.getString("overview");
            String posterPath = result.getString("poster_path");
            Long popularity = result.getLong("popularity");

            FilmsResponse filmsResponse = new FilmsResponse();
            filmsResponse.setTitle(title);
            filmsResponse.setReleaseDate(releaseDate);
            filmsResponse.setOverview(overview);
            filmsResponse.setPosterPath(posterPath);
            responseList.add(filmsResponse);

            Films films = new Films();
            films.setTitle(title);
            films.setPopularity(popularity);
            films.setOverview(overview);
            films.setPosterPath(posterPath);
            films.setReleaseDate(releaseDate);
            filmsRepository.save(films);

        }
        return responseList;
    }

    @Override
    public List<Films> retrieveAllMovies(Long id) {
        Users users = userRepository.findUsersById(id)
                .orElseThrow(() -> new CustomNotFoundException("User not found"));
        List<Films> list = filmsRepository.findAll();
        return list;
    }

    @Override
    public SearchResponse<?> searchMoviesByName(String keyword) {
        List<Films> result = filmsRepository.findAllByTitle(keyword);
        if (result.isEmpty()) {
            return new SearchResponse<>("No Result Found", null);
        }
        return new SearchResponse<>("success", result);
    }

    @Override
    @Transactional
    public RatingResponse rateMovie(Long movieId, Long userId, RateDTO rateDTO) {
        Users users = userRepository.findUsersById(userId).orElseThrow(() -> new CustomNotFoundException("User not found"));
        Films films = filmsRepository.findFilmsById(movieId)
                .orElseThrow(() -> new CustomNotFoundException("Film not found"));

        Rating duplicate = ratingRepository.findRatingByUserIdAndFilmId(users.getId(), films.getId());
        if (duplicate != null) {
            return new RatingResponse("You cannot rate a film twice");
        }
        if (rateDTO.getRate() <= 0 || rateDTO.getRate() > 5) {
            return new RatingResponse("Rating out of range");
        }
        Rating rating1 = new Rating();
        rating1.setRate(rateDTO.getRate());
        rating1.setRatedAt(LocalDateTime.now());
        rating1.setUsers(users);
        rating1.setFilms(films);
        rating1.setAverageRate(this.calculateAverageRating(films.getId()));
        ratingRepository.save(rating1);
        List<Rating> listOfRatedFilms  = ratingRepository.rateList(films.getId());

        Films films1 = filmsRepository.findFilmsById(films.getId()).get();
        films1.setAverageRating(this.calculateAverageRating(films1.getId()));
        filmsRepository.save(films1);

        return new RatingResponse("Film is successfully rated: "+rateDTO.getRate(), users.getName(), films.getTitle(), listOfRatedFilms.size(), LocalDateTime.now());
    }

    @Override
    public Double calculateAverageRating(Long id) {
        Films films = filmsRepository.findFilmsById(id).get();
         List<Users> totalUsers = ratingRepository.findRatingsByFilms(films).stream().map(Rating::getUsers).collect(Collectors.toList());

        List<Rating> filmList = ratingRepository.findRatingsByFilms(films);
        Double sumRating = 0.0;
        sumRating = Double.valueOf(filmList.stream()
                .mapToInt(Rating::getRate).sum());
        System.out.println("sumRating "+sumRating);
        System.out.println("userSize "+totalUsers.size());

        System.out.println("rate "+filmList.size());
        System.out.println("Sum "+sumRating);

        Double averageRate = 0.0;
        if (totalUsers.isEmpty()) {
            averageRate = Double.valueOf(sumRating);
        }else {
            averageRate = Double.valueOf(sumRating / totalUsers.size());

        }
        return averageRate;
    }

    @Override
    public Double retrieveAverageRating(Long id) {
        Rating averageRate = ratingRepository.findRatingByAverageRate(id);
        return averageRate.getAverageRate();
    }

    @Override
    public List<Films> getRecommendations(Long userId) {
        List<Rating> userRatings = ratingRepository.findRatingByUsersId(userId);

        Map<Long, Integer> movieRatings = new HashMap<>();
        for (Rating rating : userRatings) {
            movieRatings.put(rating.getFilms().getId(), rating.getRate());
        }
        List<Films> allMovies = filmsRepository.findAll();
        Map<Films, Double> movieScores = new HashMap<>();
        for (Films movie : allMovies) {
            double score = 0.0;
            for (Map.Entry<Long, Integer> entry : movieRatings.entrySet()) {
                Films ratedMovie = filmsRepository.findById(entry.getKey()).orElse(null);
                if (ratedMovie != null) {
                    score += cosineSimilarity(movie.getOverview(), ratedMovie.getOverview()) * entry.getValue();
                }
            }
            movieScores.put(movie, score);
        }
        List<Films> recommendedMovies = new ArrayList<>(movieScores.keySet());
        Collections.sort(recommendedMovies, (m1, m2) -> Double.compare(movieScores.get(m2), movieScores.get(m1)));
        return recommendedMovies.subList(0, Math.min(recommendedMovies.size(), 10));
    }

    private double cosineSimilarity(String s1, String s2) {
        // Implement cosine similarity algorithm to compare the description of two movies
        String[] words1 = s1.split("\\W+");
        String[] words2 = s2.split("\\W+");
        Map<String, Integer> frequencyMap1 = new HashMap<>();
        Map<String, Integer> frequencyMap2 = new HashMap<>();

        for (String word : words1) {
            frequencyMap1.put(word, frequencyMap1.getOrDefault(word, 0) + 1);
        }

        for (String word : words2) {
            frequencyMap2.put(word, frequencyMap2.getOrDefault(word, 0) + 1);
        }

        Set<String> words = new HashSet<>();
        words.addAll(frequencyMap1.keySet());
        words.addAll(frequencyMap2.keySet());

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (String word : words) {
            int count1 = frequencyMap1.getOrDefault(word, 0);
            int count2 = frequencyMap2.getOrDefault(word, 0);

            dotProduct += count1 * count2;
            magnitude1 += count1 * count1;
            magnitude2 += count2 * count2;
        }

        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        } else {
            return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
        }
    }


}
