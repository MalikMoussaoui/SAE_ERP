CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE department (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    label TEXT NOT NULL,
);

CREATE TABLE app_user (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    display_name TEXT NOT NULL,
    phone TEXT,
    role TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'ACTIF',
    department_id UUID REFERENCES department(id),
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE competency (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL
);

CREATE TABLE ue (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    department_id UUID REFERENCES department(id),
    title TEXT NOT NULL,
    semester INT NOT NULL
);

CREATE TABLE resource (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ue_id UUID REFERENCES ue(id),
    title TEXT NOT NULL,
    hours_cm INT DEFAULT 0,
    hours_td INT DEFAULT 0,
    hours_tp INT DEFAULT 0
);

CREATE TABLE sae (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ue_id UUID REFERENCES ue(id),
    title TEXT NOT NULL,
    description TEXT,
    hours_sae INT NOT NULL
);

CREATE TABLE resource_sheet (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    status TEXT NOT NULL DEFAULT 'DRAFT',
    objectives TEXT NOT NULL,
    modalities TEXT NOT NULL,
    hours_cm INT NOT NULL DEFAULT 0,
    hours_td INT NOT NULL DEFAULT 0,
    hours_tp INT NOT NULL DEFAULT 0,
    version INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE course (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sae_id UUID REFERENCES sae(id),
    resource_id UUID REFERENCES resource(id),
    hours INT NOT NULL DEFAULT 0
)

CREATE TABLE list_prof_course (
    id_course UUID REFERENCES course(id) ON DELETE CASCADE,
    id_prof UUID REFERENCES app_user(id) ON DELETE CASCADE,
    PRIMARY KEY (id_course, id_prof)
)

CREATE TABLE resource_sheet_competency (
    resource_sheet_id UUID REFERENCES resource_sheet(id) ON DELETE CASCADE,
    competency_id UUID REFERENCES competency(id) ON DELETE CASCADE,
    PRIMARY KEY (resource_sheet_id, competency_id)
);

CREATE TABLE tac_entry (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    department_id UUID REFERENCES department(id),
    date DATE NOT NULL,
    observation TEXT NOT NULL,
    origin TEXT,
    thematic TEXT,
    correction TEXT,
    analyse_causes TEXT,
    action TEXT,
    due_date DATE,
    status TEXT,
    commentary TEXT
);

CREATE TABLE feedback (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    author_id UUID REFERENCES app_user(id),
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    commentary TEXT NOT NULL
);