DROP TABLE IF EXISTS t_order_commodity;
DROP TABLE IF EXISTS t_commodity;
DROP TABLE IF EXISTS t_order;
DROP TABLE IF EXISTS t_user;

CREATE TABLE IF NOT EXISTS t_user(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    create_time TIMESTAMP,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS t_order(
    id BIGINT NOT NULL AUTO_INCREMENT,
    user INT NOT NULL,
    FOREIGN KEY (user) REFERENCES t_user(id),
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS t_commodity(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS t_order_commodity(
    t_order BIGINT NOT NULL,
    t_commodity BIGINT NOT NULL,
    count INT NOT NULL DEFAULT 0,
    FOREIGN KEY (t_order) REFERENCES t_order(id),
    FOREIGN KEY (t_commodity) REFERENCES t_commodity(id),
    PRIMARY KEY (t_order, t_commodity)
);