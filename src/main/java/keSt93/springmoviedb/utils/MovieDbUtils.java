package keSt93.springmoviedb.utils;

import keSt93.springmoviedb.repository.MovieRepository;

import javax.annotation.PostConstruct;

public final class MovieDbUtils {

    public static int[] getCalculatedMovieTime(MovieRepository movieRepository) {
        int calculatedTime[] = new int[2];
        int totalWastedMinutes = movieRepository.getTotalWastedMinutes();

        calculatedTime[0] = totalWastedMinutes / 60;
        calculatedTime[1] = totalWastedMinutes % 60;
        return calculatedTime;
    }

}
