package com.jaya.cmt.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jaya.cmt.Collaborators;
import com.jaya.cmt.Paper;
import com.jaya.cmt.repository.PaperRepository;

@Service
public class service {

	@Autowired
    private emailService emailService;
    @Autowired
    private PaperRepository repository;


	public ResponseEntity<String> uploadpaper(String title, String abstractContent, List<String> collaborators,
			MultipartFile file,String userEmail,Integer confId) {
        String uploadDir = "c:/uploads/";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = uploadDir + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath)); 
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Paper paper = new Paper();
        paper.setTitle(title);
        paper.setAbstractContent(abstractContent);
        paper.setCollaborators(collaborators);
        paper.setFilePath(filePath); 
        paper.setUserMail(userEmail);
        paper.setConfId(confId);
        repository.save(paper); 

        String email = userEmail;
        emailService.sendEmail(email, "Paper Submission Confirmation", "Hello,\r\n"
        		+ "\r\n"
        		+ "The following submission has been created.\r\n"
        		+ "\r\n"
        		
        		+ "\r\n"
        		+ "Paper Title: "+title+"\r\n"
        		+ "\r\n"
        		+ "Abstract:"+abstractContent+"\r\n"
        		
        		+ "\r\n"
        		
        		
        		+ "\r\n"
        		+ "Thanks,\r\n"
        		+ "CMT team.");
        return new ResponseEntity<>("Paper uploaded successfully", HttpStatus.OK);
	}


	public ResponseEntity<String> updatePaper(Integer id, String title, String abstractContent,
			List<String> collaborators, MultipartFile file,String userEmail,Integer confId) {
		
		Optional<Paper> optionalPaper = repository.findById(id);
		
		if (!optionalPaper.isPresent()) {
            return new ResponseEntity<>("Paper not found", HttpStatus.NOT_FOUND);
        }

        Paper paper = optionalPaper.get();
        paper.setTitle(title);
        paper.setAbstractContent(abstractContent);
        paper.setCollaborators(collaborators);

        if (file != null && !file.isEmpty()) {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = uploadDir + file.getOriginalFilename();
            try {
                file.transferTo(new File(filePath));
                paper.setFilePath(filePath);
                
            } catch (IOException e) {
                return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        repository.save(paper);
        String email = userEmail;
        emailService.sendEmail(email, "Paper Update Confirmation",  "Hello,\r\n"
        		+ "\r\n"
        		+ "The following submission has been created.\r\n"
        		+ "\r\n"
        		
        		+ "\r\n"
        		+ "Paper Title: "+title+"\r\n"
        		+ "\r\n"
        		+ "Abstract:"+abstractContent+"\r\n"
        		
        		+ "\r\n"
        		
        		
        		+ "\r\n"
        		+ "Thanks,\r\n"
        		+ "CMT team.");
        return new ResponseEntity<>("Paper updated successfully", HttpStatus.OK);
    }


	public ResponseEntity<Paper> getPaperById(Integer id) {
		Optional<Paper> paper = repository.findById(id);

        if (!paper.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(paper.get(), HttpStatus.OK);
	}


	public ResponseEntity<String> deletePaper(Integer id) {
		Optional<Paper> paper = repository.findById(id);

        if (!paper.isPresent()) {
            return new ResponseEntity<>("Paper not found", HttpStatus.NOT_FOUND);
        }

        
        repository.deleteById(id);
        String email = getUserEmailById(id);
        emailService.sendEmail(email, "Paper Deletion Confirmation", "Your paper has been deleted successfully.");
        return new ResponseEntity<>("Paper deleted successfully", HttpStatus.OK);
	}


	public ResponseEntity<Paper> validPaper(String userEmail, Integer confId) {
		Optional<Paper> paper = repository.findByUserEmailAndConfId(userEmail, confId);
        
        if (paper.isPresent()) {
            return new ResponseEntity<>(paper.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
	}
	private String getUserEmailById(Integer userId) {
        // Fetch the user's email from the database based on userId
        return "user@example.com"; // Replace with actual email fetching logic
    }


	public List<Paper> paperByMail(String mail) {
		return repository.findByUserMail(mail);
	}
}
