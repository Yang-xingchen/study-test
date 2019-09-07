package spring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class A {

    @Autowired
    private B b;

    public void print(){
        log.info("class A print");
        b.printf();
    }

}
