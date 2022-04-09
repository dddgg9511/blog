package com.choo.blog.domain.users;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public User join(UserRegistData registData){
        User user = modelMapper.map(registData, User.class);

        return userRepository.save(user);
    }
}
