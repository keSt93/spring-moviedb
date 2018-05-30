package keSt93.springmoviedb.controller;

import keSt93.springmoviedb.entities.Movie;
import keSt93.springmoviedb.entities.MovieComments;
import keSt93.springmoviedb.entities.User;
import keSt93.springmoviedb.repository.MovieCommentsRepository;
import keSt93.springmoviedb.repository.MovieRepository;
import keSt93.springmoviedb.repository.UserRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Date;

@Controller
public class CommentController {

    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private MovieCommentsRepository movieCommentsRepository;

    public CommentController(
            MovieRepository movieRepository,
            UserRepository userRepository,
            MovieCommentsRepository movieCommentsRepository
    ) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.movieCommentsRepository = movieCommentsRepository;
    }

    // Add Comment
    @PostMapping(value = "/actions/addCommentAction/{id}")
    private String saveComment(MovieComments newComment, Principal principal, @PathVariable int id) {
        Movie currentMovie = movieRepository.findById(id);
        User currentUser = userRepository.findByUsernameEquals(principal.getName());


        if (StringUtils.isNotEmpty(newComment.getComment()) && currentMovie != null) {
            newComment.setUser(currentUser);
            newComment.setComment(newComment.getComment());
            newComment.setCreationDate(new Date());
            newComment.setMovie(currentMovie);
            movieCommentsRepository.save(newComment);
            return "redirect:/m/movies/" + id;
        } else {
            return "redirect:/m/movies/" + id + "?commentfail=true";
        }
    }

    @RequestMapping(value = "/actions/deleteCommentAction/{movieId}/{commentId}")
    private String deleteComment(Principal principal, @PathVariable int movieId, @PathVariable int commentId) {
        MovieComments currentComment = movieCommentsRepository.findOne(commentId);
        User currentUser = userRepository.findByUsernameEquals(principal.getName());

        String currentUsername = currentUser.getUsername();
        String commentUsername = currentComment.getUser().getUsername();

        if (commentUsername.equals(currentUsername)) {
            currentComment.setMovie(null);
            currentComment.setUser(null);
            movieCommentsRepository.delete(currentComment);
            return "redirect:/m/movies/" + movieId;
        } else {
            return "redirect:/m/movies/" + movieId + "?commentfail=true";
        }
    }

}
