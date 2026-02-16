package com.example.service;

import com.example.dtos.request.LabelRequest;
import com.example.entity.Label;
import com.example.mapper.LabelMapper;
import com.example.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelMapper labelMapper;

    public Label create(LabelRequest labelRequest){
        Label entity = labelMapper.toEntity(labelRequest);
        return this.labelRepository.save(entity);
    }

    public List<Label> findAll(){
        return labelRepository.findAll();
    }

    public Label update(Long labelId, LabelRequest labelRequest){
        Label entity = labelRepository.findById(labelId).orElseThrow();
        entity.setName(labelRequest.getName());

        return labelRepository.save(entity);
    }

    public void delete(Long labelId){
        labelRepository.deleteById(labelId);
    }

}
