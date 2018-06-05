package com.test.data.service;

import com.test.data.domain.Movie;
import com.test.data.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagesService {

    @Autowired
    public MovieRepository movieRepository;

    public Page<Movie> findAllMovie(Pageable pageable){
        Page<Movie> moviePage = movieRepository.findAll(pageable);
        return moviePage;
    }
}
