package com.bg.doubt.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    ModelMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = new ModelMapper();
    }

    public UserDto JoinUser(UserDto user){
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        userEntity.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        UserEntity createdUser = userRepository.save(userEntity);


        return mapper.map(createdUser, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException(userId));


        return new User(userEntity.getUserId(), userEntity.getUserPassword(), new ArrayList<>());
    }
}
