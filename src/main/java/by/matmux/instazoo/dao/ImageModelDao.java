package by.matmux.instazoo.dao;

import by.matmux.instazoo.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageModelDao extends JpaRepository<ImageModel, Long> {

    Optional<ImageModel> findByUserId(Long id);

    Optional<ImageModel> findByPostId(Long id);
}
