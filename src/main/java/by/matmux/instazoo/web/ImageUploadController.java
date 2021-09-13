package by.matmux.instazoo.web;

import by.matmux.instazoo.entity.ImageModel;
import by.matmux.instazoo.payload.response.MessageResponse;
import by.matmux.instazoo.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageUploadController {
    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);
        return new ResponseEntity<>(new MessageResponse("Image uploaded successfully"), HttpStatus.OK);
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId, @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageUploadService.uploadImageForPost(file, principal, Long.parseLong(postId));
        return new ResponseEntity<>(new MessageResponse("Image uploaded successfully"), HttpStatus.OK);
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getImageForUser(Principal principal) {
        ImageModel userImage = imageUploadService.getImageToUser(principal);

        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageModel> getImageForPost(@PathVariable("postId") String postId) {
        ImageModel postImage = imageUploadService.getImageToPost(Long.parseLong(postId));

        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }
}
