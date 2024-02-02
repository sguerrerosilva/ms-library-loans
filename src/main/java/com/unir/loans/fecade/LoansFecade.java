package com.unir.loans.fecade;

import com.unir.loans.model.BookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoansFecade {

    @Value("${getBook.url}")
    private String getBookUrl;

    @Value("${patchBook.url}")
    private String patchBookUrl;

    private final RestTemplate restTemplate;

    public BookDto getBook(Long id){

        try {
            String url = String.format(getBookUrl, id);
            log.info("Getting book with ID {}. Request to {}", id, url);
            return restTemplate.getForObject(url, BookDto.class);
        } catch (HttpClientErrorException e) {
            log.error("Client Error: {}, book with ID {}", e.getStatusCode(), id);
            return null;
        } catch (HttpServerErrorException e) {
            log.error("Server Error: {}, book with ID {}", e.getStatusCode(), id);
            return null;
        } catch (Exception e) {
            log.error("Error: {}, book with ID {}", e.getMessage(), id);
            return null;
        }

    }

    public BookDto patchBook(Long id, String body){
        try {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setConnectTimeout(5000);
            restTemplate.setRequestFactory(requestFactory);
            String url = String.format(patchBookUrl,id);
            log.info(url);
            return restTemplate.patchForObject(url, body, BookDto.class);
        } catch (HttpClientErrorException e) {
            log.error("Client Error: {}, book with ID {}", e.getStatusCode(), id);
            return null;
        } catch (HttpServerErrorException e) {
            log.error("Server Error: {}, book with ID {}", e.getStatusCode(), id);
            return null;
        } catch (Exception e) {
            log.error("Error: {}, book with ID {}", e.getMessage(), id);
            return null;
        }
    }
}
