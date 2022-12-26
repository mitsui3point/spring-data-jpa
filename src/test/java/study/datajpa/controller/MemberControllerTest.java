package study.datajpa.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    /**
     * webAppContextSetup(Integration test) vs standaloneSetup(Unit test)<p/>
     * https://velog.io/@hanblueblue/Spring-mvc-standaloneSetup-vs-webAppContextSetup
     */
    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();//WebConversionService
        //mvc = MockMvcBuilders.standaloneSetup(memberController).build();//DefaultFormattingConversionService
        member = Member.builder().username("username1").build();
        memberRepository.save(member);
    }

    @Test
    void findMemberTest() throws Exception {
        //given

        //when
        ResultActions perform = mvc.perform(get("/members/1"));
        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("username1"));
    }

    /**
     * use {@link AutoConfigureMockMvc}
     * <p/>
     * expect {@link DataBinder} @code {@link WebConversionService} (web application conversionService)<br/>
     * but {@link DataBinder} @code {@link DefaultFormattingConversionService} (mocktest conversionService)
     */
    @Test
    void findDomainClassConverterMemberTest() throws Exception {
        //given

        //when
        ResultActions perform = mvc.perform(get("/members/domain/1"));
        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("username1"));
    }
}
