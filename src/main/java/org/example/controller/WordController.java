package org.example.controller;


import org.example.model.User;
import org.example.model.Word;
import org.example.repository.WordRepository;
import org.example.response.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("words")
public class WordController {

    @Autowired
    private WordRepository wordRepository;

    @GetMapping
    public ResponseEntity<CustomResponse> getAllWords() {

        if (wordRepository.count() < 1) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("No data found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }


        CustomResponse response = new CustomResponse("success", this.wordRepository.findAll());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse> getWordByID(@PathVariable int id) {
        if (!wordRepository.existsById(id)) {
            CustomResponse errResponse = new CustomResponse("Error", new Error("Id is not in database!"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errResponse);
        }
        Word word = this.wordRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found!"));
        CustomResponse res = new CustomResponse("Success", word);
        return ResponseEntity.ok(res);
    }
}
