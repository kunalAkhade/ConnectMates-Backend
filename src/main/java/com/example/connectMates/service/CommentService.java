package com.example.connectMates.service;

import com.example.connectMates.Utilities.MyUtilities;
import com.example.connectMates.dao.CommentDao;
import com.example.connectMates.dao.PostDao;
import com.example.connectMates.dao.UserCategoryDao;
import com.example.connectMates.dao.UserDao;
import com.example.connectMates.entities.Comment;
import com.example.connectMates.entities.Post;
import com.example.connectMates.entities.User;
import com.example.connectMates.entities.UserCategory;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@Service
public class CommentService {

    @Autowired
    private MyUtilities utilities;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCategoryDao userCategoryDao;

    public String addComment(Map<String, String> request) {
        Comment comment = new Comment();

        Optional<Post> post = postDao.findById(Long.valueOf(request.get("post")));
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> postUser = userDao.findById(post.get().getUser().getId());


        Optional<User> user = Optional.ofNullable(userDao.findByUsername(userDetails.getUsername()));

        if (post.isPresent() && user.isPresent() && (postUser.isPresent() && utilities.checkIfFollow(postUser.get(),user.get()))) {
            comment.setContent(request.get("content"));
            comment.setPost(post.get());
            comment.setUser(user.get());
            commentDao.save(comment);
            //  calculating score
           float score = calculateCommentBasedScore(request.get("content"));
           UserCategory userCategory = userCategoryDao.findUserCategoryByUserAndCategory(post.get().getUser().getId(),post.get().getCategory().getId());
           float maxScore = userCategoryDao.findMaxCommentScore(post.get().getCategory().getId());
            System.out.println("max"+maxScore);
           if(userCategory != null ){
               userCategory.setCommentScore((userCategory.getCommentScore()+(score/maxScore))/2);
           } else {
               userCategory.setUser(post.get().getUser());
               userCategory.setCategory(post.get().getCategory());
               userCategory.setCommentScore(maxScore==0 ? score: score/maxScore);
           }
           userCategoryDao.save(userCategory);
           return "comment added";
        }
        return "Invalid comment";

    }



    public Object getComment(Long id) {
       Optional<Post> post = postDao.findById(id);
        if(post.isPresent()){
            Optional<User> user = userDao.findById(post.get().getUser().getId());
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User otherUser = userDao.findByUsername(userDetails.getUsername());
            if( user.isPresent() && utilities.checkIfFollow(user.get(),otherUser )){
                return post.<Object>map(Post::getComments).orElse(null);
            }
        }
        return null;
    }


    public static float calculateCommentBasedScore(String text){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,parse,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        float sum=0;
        int line=0;
        for (CoreSentence sentence : document.sentences()) {
            System.out.println(sentence.sentiment());
            sum += getSentimentValue(sentence.sentiment());
            System.out.println(sum);
            line++;
        }
        return line == 0 ? 0 :  sum/line;
    }

    private static int getSentimentValue(String sentiment) {
        switch (sentiment) {
            case "Very negative": return 0;
            case "Negative": return 1;
            case "Neutral": return 2;
            case "Positive": return 3;
            case "Very positive": return 4;
            default: return 0; // Unknown sentiment
        }
    }





}
