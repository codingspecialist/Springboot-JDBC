package shop.mtcoding.jdbcstudy.model;

import lombok.*;

@Getter
public class Customer {
    private Long id;
    private String name;
    private String tel;

    public Customer() {
        // Jackson은 리플렉션으로 발동하기 때문에 setter가 없어도 private 변수에 접근 가능
        System.out.println("Jackson 발동시 디폴트 생성자 실행");
    }

    public Customer(Long id, String name, String tel){
        System.out.println("조회시에 mapper 동작할 때 풀 생성자 실행");
        this.id = id;
        this.name = name;
        this.tel = tel;
    }

    public void update(String name, String tel){
        this.name = name;
        this.tel = tel;
    }
}
