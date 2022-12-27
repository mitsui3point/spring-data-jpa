package study.datajpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class MemberControllerTest {


    public static final String FIND_DOMAIN_CLASS_CONVERTER_MEMBER = "/members/domain/";
    public static final String FIND_PAGING_MEMBERS = "/members";
    private static final String FIND_MEMBER = "/members/";
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper om;

    private Member member;

    /**
     * webAppContextSetup(Integration test) vs standaloneSetup(Unit test)<p/>
     * https://velog.io/@hanblueblue/Spring-mvc-standaloneSetup-vs-webAppContextSetup
     */
    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();//WebConversionService
        //mvc = MockMvcBuilders.standaloneSetup(memberController).build();//DefaultFormattingConversionService
        for (int i = 0; i < 100; i++) {
            member = Member.builder().username("username" + i).age(i).build();
            memberRepository.save(member);
        }
    }

    @Test
    void findMemberTest() throws Exception {
        //given

        //when
        ResultActions perform = mvc.perform(get(FIND_MEMBER + 1));
        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("username0"));
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
        ResultActions perform = mvc.perform(get(FIND_DOMAIN_CLASS_CONVERTER_MEMBER + 1));
        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("username0"));
    }

    @Test
    void findMembersTest() throws Exception {
        //given
        int page = 4;
        int size = 12;
        String sort1 = "id";
        String sort2 = "username";
        String direction = "desc";
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort1, sort2));
        String expected = om.writeValueAsString(memberRepository.findAll(pageRequest)
                .map(m -> MemberDto.builder()
                        .member(m)
                        .build()
                ));

        //when
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("size", String.valueOf(size));
        params.add("sort", String.join(",", sort1, direction));
        params.add("sort", String.join(",", sort2, direction));
        ResultActions perform = mvc.perform(get(FIND_PAGING_MEMBERS)
                .queryParams(params));
        //then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expected));
    }
}
