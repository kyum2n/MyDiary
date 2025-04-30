package com.example.mydiary.service;

import org.springframework.stereotype.Service;

import com.example.mydiary.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyUserService implements MyUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails load
}
