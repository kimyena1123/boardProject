package com.fastcampus.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;
//@Configuration: 이걸 사용함으로써 JPA config는 configuration bean이 된다. 각종 설정을 잡을 때 사용
//@EnableJpaAuditing: 이것으로 JPA auditing 기능을 추가 활성화 시킬 것이다.
@EnableJpaAuditing
@Configuration
public class JpaConfig{

    //auditing 할 때 사람 이름을 넣어주기 위한 config를 잡아줄 수 있다.
    //<> 안에 들어갈 내용은 id가 되는데, 사람 이름을 넣어줄거니까 문자열(string)으로 해주고
    @Bean
    public AuditorAware<String> auditorAware() {
        //임의의 이름 넣어주기. 이렇게 함으로써 이제 auditing할 때마다 사람 이름은 "yena"로 넣게 된다.
        //따라서 이건 나중에 반드시 바꿔야 한다.
        //지금은 누가 입력을 할 지를 인증 기능을 따로 제공하지 않았으니까 식별할 수 없는 상태이다.
        //인증 기능을 붙이게 된다면 누가 로그인을 했는지 알게 되니까 그 내용을 토대로 이름을 뽑아낼 수 있을 것이다(그게 아이디가 됐든 닉네임이 됐든지)
        return () -> Optional.of("yena"); // TODO: 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정하자
    }
}
