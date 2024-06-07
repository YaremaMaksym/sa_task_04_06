CREATE TABLE companies (
     id                     UUID PRIMARY KEY,
     name                   VARCHAR(255) NOT NULL,
     registration_number    VARCHAR(255) NOT NULL,
     address                VARCHAR(255) NOT NULL,
     created_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reports (
    id                      UUID PRIMARY KEY,
    company_id              UUID NOT NULL REFERENCES companies(id),
    report_date             TIMESTAMP NOT NULL,
    total_revenue           DECIMAL NOT NULL,
    net_profit              DECIMAL NOT NULL,
    CONSTRAINT fk_report_company FOREIGN KEY (company_id) REFERENCES companies(id)
);