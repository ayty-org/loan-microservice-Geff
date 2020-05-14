package br.com.phoebus.microservice.biblioteca.loan.libraryloan.service;

import br.com.phoebus.microservice.biblioteca.loan.libraryloan.LibraryUserDTO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserAndBookServiceImpl implements UserAndBookService {
    @LoadBalanced
    private final RestTemplate restTemplate;
    private final String URI = "http://USERANDBOOK/v1/libraryUser/{id}";

    @Override
    @HystrixCommand(fallbackMethod = "defaultLibraryUser")
    public LibraryUserDTO findUserOfLoan(Long id) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);
        return restTemplate.getForObject(URI, LibraryUserDTO.class, params);
    }

    private LibraryUserDTO defaultLibraryUser(Long id) {
        return new LibraryUserDTO();
    }
}
