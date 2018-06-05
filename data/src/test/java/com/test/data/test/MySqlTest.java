package com.test.data.test;

import com.test.data.domain.Movie;
import com.test.data.repository.MovieRepository;
import com.test.data.service.PagesService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfiguration.class})
public class MySqlTest {
    Logger logger = LoggerFactory.getLogger(MySqlTest.class);

    @Autowired
    MovieRepository movieRepository;

    @Before
    public void initData() {
        movieRepository.deleteAll();

        Movie movie = new Movie();
        movie.setName("金刚狼");
        movie.setCreateDate(new Date());
        movie.setPhoto("/pic.png");

        movieRepository.save(movie);
        Assert.notNull(movie.getId());
        logger.info("=============初始化完成=============");
    }

    @Test
    public void pageService_findAllMovieTest() {
        PagesService pagesService=new PagesService();
        pagesService.movieRepository= this.movieRepository;
        Page<Movie> moviePage = pagesService.findAllMovie(new PageRequest(0, 10, new Sort(Sort.Direction.ASC, "id")));
        logger.info("============= " + moviePage.getContent().size());
    }
}
