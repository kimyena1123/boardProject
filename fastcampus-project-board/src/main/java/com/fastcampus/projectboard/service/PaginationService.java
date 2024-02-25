package com.fastcampus.projectboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    //페이지네이션 바의 길이를 정해야 하는데, 이건 외부에서 받게끔 디자인할 수도 있지만, 이번에는 그냥 안에 상태값으로 존재하게 해서 쉽게 구현해보자
    private static final int BAR_LENGTH = 5;

    //페이지네이션 바를 어떻게 데이터로 내려줄 것인가? 리스트 형태 숫자로.
    //숫자 리스트를 바아서 view에서 그려준다.
    //매개변수1: 현재 내가 어느 페이지에 있는지: currentPageNumber
    //매개변수2: 계산을 하려면 전체 페이지 수를 알아야 한다. 그러면 끝 숫자를 계산하는데 도움이 되기에.
    public List<Integer> getPaginationBarNumbers(int currentpageNumber, int totalPages){

        //시작 번호와 끝번호를 계산해볼 것이다
        int startNumber = Math.max(currentpageNumber - (BAR_LENGTH / 2), 0); // 음수를 막아줘야 한다(math사용)
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPages);

        //이걸로 리스트를 만들어준다
        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    //한가지 getter를 열어놔서 현재 이 서비스가 알고 있는 bar length를 원한다면 조회할 수 있게끔 열어놓자
    public int currentBarlength(){
        return BAR_LENGTH;
    }


}
