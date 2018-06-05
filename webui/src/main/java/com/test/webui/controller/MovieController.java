package com.test.webui.controller;

import com.test.data.domain.Actor;
import com.test.data.domain.Movie;
import com.test.data.repository.ActorRepository;
import com.test.data.repository.MovieRepository;
import com.test.data.service.PagesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private static Logger logger=LoggerFactory.getLogger(MovieController.class);

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    PagesService pagesService;

    @RequestMapping(value = "/index")
    public ModelAndView index(){
        return new ModelAndView("movie/index");
    }

    @RequestMapping("/new")
    public ModelAndView create(ModelMap modelMap){
        String[] files={"/images/movie/蜘蛛侠.jpg","/images/movie/金刚狼.jpg"};
        modelMap.addAttribute("files",files);
        return new ModelAndView("movie/new");
    }

    @RequestMapping(value = "/{id}")
    public ModelAndView show(ModelMap modelMap, @PathVariable Long id){
        Movie movie=movieRepository.findOne(id);
        modelMap.addAttribute("movie",movie);
        return new ModelAndView("movie/show");
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public String save(Movie movie){
        movieRepository.save(movie);
        logger.info("新增->ID={}",movie.getId());
        return "1";
    }

    @RequestMapping(value = "/edit/{id}")
    public ModelAndView update(ModelMap modelMap,@PathVariable Long id){
        Movie movie=movieRepository.findOne(id);
        String[] files={"/images/movie/蜘蛛侠.jpg","images/movie/金刚狼.jpg"};
        String[] rolelist={"彼得·帕克","格温·史黛西","康纳斯博士"};
        List<Actor> actors = actorRepository.findAll();

        modelMap.addAttribute("files",files);
        modelMap.addAttribute("rolelist",rolelist);
        modelMap.addAttribute("movie",movie);
        modelMap.addAttribute("actors",actors);

        return new ModelAndView("movie/edit");
    }

    @RequestMapping(value = "/upate",method = RequestMethod.POST)
    public String update(Movie movie, HttpServletRequest request){
        String rolename=request.getParameter("rolename");
        String actorid=request.getParameter("actorid");

        Movie old=movieRepository.findOne(movie.getId());
        old.setName(movie.getName());
        old.setPhoto(movie.getPhoto());
        old.setCreateDate(movie.getCreateDate());

        if(!StringUtils.isEmpty(rolename) && !StringUtils.isEmpty(actorid)){
            Actor actor=actorRepository.findOne(new Long(actorid));
            old.addRole(actor,rolename);
        }
        movieRepository.save(old);
        logger.info("修改->ID = "+old.getId());
        return "1";
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.GET)
    public String delete(@PathVariable Long id){
        Movie movie=movieRepository.findOne(id);
        movieRepository.delete(movie);
        logger.info("删除 -> ID = "+id);
        return "1";
    }

    @RequestMapping(value = "/list")
    public Page<Movie> list(HttpServletRequest request){
        String name=request.getParameter("name");
        String page=request.getParameter("page");   //多少页
        String size=request.getParameter("size");
        Pageable pageable=new PageRequest(page==null ? 0:Integer.parseInt(page),size==null?10:Integer.parseInt(size),new Sort(Sort.Direction.DESC,"id"));

        return pagesService.findAllMovie(pageable);
    }
}
























