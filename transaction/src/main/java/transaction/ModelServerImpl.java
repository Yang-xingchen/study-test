package transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"AlibabaTransactionMustHaveRollback", "AlibabaLowerCamelCaseVariableNaming"})
@Service
public class ModelServerImpl implements ModelServer{

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelServer modelServer;

    @Override
    public long count(String value) {
        return modelRepository.countByValue(value);
    }


    // =========================================================
    //                     test method
    // =========================================================


    /**
     * 0
     * 出现异常，回滚
     */
    @Transactional
    @Override
    public void defaultTransaction() {
        modelRepository.save(Model.builder().value("A").build());
        throw new RuntimeException();
    }

    /**
     * 2
     * 内嵌方法未进行增强，事务失效
     */
    @Override
    public void OITrTh() {
        modelRepository.save(Model.builder().value("B").build());
        TrTh("B");
    }

    /**
     * 0
     * 外层方法事务增强，回滚
     */
    @Transactional
    @Override
    public void OTrITh() {
        modelRepository.save(Model.builder().value("C").build());
        Th("C");
    }

    /**
     * 1
     * 内嵌异常已增强，回滚
     * 外层未增强，未进行回滚
     */
    @Override
    public void OISTrTh() {
        modelRepository.save(Model.builder().value("D").build());
        modelServer.TrTh("D");
    }

    /**
     * 0
     * 出现异常，回滚
     */
    @Transactional
    @Override
    public void OTrThI() {
        modelRepository.save(Model.builder().value("E").build());
        innerSave();
        throw new RuntimeException();
    }

    /**
     * 1
     * 外层方法捕获异常，未回滚
     * 内嵌方法回滚
     */
    @Transactional
    @Override
    public void OTryISTrNewTh() {
        modelRepository.save(Model.builder().value("F").build());
        try {
            modelServer.TrNewTh("F");
        } catch (Exception ignore) {
        }
    }

    /**
     * 0
     * 外层方法存在异常，回滚
     */
    @Transactional
    @Override
    public void OISTrNewTh() {
        modelRepository.save(Model.builder().value("G").build());
        modelServer.TrNewTh("G");
    }

    /**
     * 0
     * 内嵌异常捕获，未回滚
     */
    @Transactional
    @Override
    public void OTryITh() {
        modelRepository.save(Model.builder().value("H").build());
        try {
            modelServer.TrTh("H");
        } catch (Exception ignore) {
        }
    }


    // =========================================================
    //                     inner method
    // =========================================================

    @Override
    public void innerSave() {
        modelRepository.save(Model.builder().value("E").build());
    }

    @Override
    public void Th(String v) {
        modelRepository.save(Model.builder().value(v).build());
        throw new RuntimeException();
    }

    @Transactional
    @Override
    public void TrTh(String v) {
        modelRepository.save(Model.builder().value(v).build());
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void TrNewTh(String v) {
        modelRepository.save(Model.builder().value(v).build());
        throw new RuntimeException();
    }
}
