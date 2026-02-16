package com.example.controller;

import com.example.dtos.request.LabelRequest;
import com.example.dtos.response.LabelResponse;
import com.example.entity.Label;
import com.example.mapper.LabelMapper;
import com.example.service.LabelService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("labels")
public class LabelController {

    @Autowired
    private LabelService labelService;
    @Autowired
    private LabelMapper labelMapper;

    @PostMapping("")
    public LabelResponse create(@RequestBody LabelRequest labelRequest){
        return labelMapper.toResponse(labelService.create(labelRequest));
    }

    @GetMapping("")
    public List<LabelResponse> findAll(){
        return labelService.findAll().stream().map(labelMapper::toResponse).toList();
    }

    @PutMapping("/{labelId}")
    public LabelResponse update(@PathVariable("labelId") Long labelId, @RequestBody LabelRequest labelRequest){
        return labelMapper.toResponse(labelService.update(labelId, labelRequest));
    }

    @DeleteMapping("/{labelId}")
    public ResponseEntity<Void> delete(@PathVariable("labelId") Long labelId){
        labelService.delete(labelId);
        return ResponseEntity.noContent().build();
    }

}
