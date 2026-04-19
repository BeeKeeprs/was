package kr.co.webee.domain.post.repository;

import kr.co.webee.domain.post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    int countByPostId(Long postId);

    List<PostComment> findByPostId(Long postId);
}
