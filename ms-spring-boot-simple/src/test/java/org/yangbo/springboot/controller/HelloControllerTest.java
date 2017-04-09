package org.yangbo.springboot.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {
	@Autowired MockMvc mockMvc;
	
	@Test
	public void test() throws Exception{
		// 1. 构建一个 mock mvc request build
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON_UTF8);
		// 2. 用 mock mvc 执行请求
		mockMvc.perform(request).andExpect(status().isOk())
		.andExpect(content().string(equalTo("你好，欢迎使用 Spring Boot Web!")));
	}
}
