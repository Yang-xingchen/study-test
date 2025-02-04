package transaction;

public interface ModelServer {

    void defaultTransaction();

    void OITrTh();

    void OTrITh();

    void Th(String v);

    void OISTrTh();

    void TrTh(String v);

    void OTrThI();

    void clear();

    long count(String a);

    void OTryITh();

    void innerSave();

    void OTryISTrNewTh();

    void OISTrNewTh();

    void TrNewTh(String v);
}
