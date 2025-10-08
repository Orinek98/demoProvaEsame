package com.dsbd.project.service;

import com.dsbd.project.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectTripService {

    @Autowired
    TripRepository repository;
}
