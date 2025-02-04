package transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"AlibabaTransactionMustHaveRollback", "AlibabaLowerCamelCaseVariableNaming"})
@Service
public class ModelServerImpl implements ModelServer{

    @Autowired
    private ModelMapper modelMapper;

    @Lazy
    @Autowired
    private ModelServer modelServer;

    @Override
    public void clear() {
        modelMapper.clear();
    }

    @Override
    public long count(String value) {
        return modelMapper.countByValue(value);
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
        modelMapper.save(new Model(null, "A"));
        throw new RuntimeException();
    }

    /**
     * 2
     * 内嵌方法未进行增强，事务失效
     */
    @Override
    public void OITrTh() {
        modelMapper.save(new Model(null, "B"));
        TrTh("B");
    }

    /**
     * 0
     * 外层方法事务增强，回滚
     */
    @Transactional
    @Override
    public void OTrITh() {
        modelMapper.save(new Model(null, "C"));
        Th("C");
    }

    /**
     * 1
     * 内嵌异常已增强，回滚
     * 外层未增强，未进行回滚
     */
    @Override
    public void OISTrTh() {
        modelMapper.save(new Model(null, "D"));
        modelServer.TrTh("D");
    }

    /**
     * 0
     * 出现异常，回滚
     */
    @Transactional
    @Override
    public void OTrThI() {
        modelMapper.save(new Model(null, "E"));
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
        modelMapper.save(new Model(null, "F"));
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
        modelMapper.save(new Model(null, "G"));
        modelServer.TrNewTh("G");
    }

    /**
     * 0
     * 内嵌异常捕获，未回滚
     */
    @Transactional
    @Override
    public void OTryITh() {
        modelMapper.save(new Model(null, "H"));
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
        modelMapper.save(new Model(null, "E"));
    }

    @Override
    public void Th(String v) {
        modelMapper.save(new Model(null, v));
        throw new RuntimeException();
    }

    @Transactional
    @Override
    public void TrTh(String v) {
        modelMapper.save(new Model(null, v));
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void TrNewTh(String v) {
        modelMapper.save(new Model(null, v));
        throw new RuntimeException();
    }
}
