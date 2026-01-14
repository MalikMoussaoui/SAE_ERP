CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE department (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    label TEXT NOT NULL
);

CREATE TABLE app_user (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    display_name TEXT NOT NULL,
    phone TEXT,
    role TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'ACTIF',
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
     id UUID PRIMARY KEY,
     titre VARCHAR(255),
     code VARCHAR(50),
     semestre VARCHAR(10),
     ue VARCHAR(100),
     departement VARCHAR(255),
     description TEXT,
     h_cm DOUBLE PRECISION,
     h_td DOUBLE PRECISION,
     h_tp DOUBLE PRECISION,
     type_evaluation VARCHAR(100),
     coefficient_ressource DOUBLE PRECISION,
     evaluations_prevues VARCHAR(255),
     note_minimale DOUBLE PRECISION,
     compensation VARCHAR(10),
     rattrapage VARCHAR(10),
     modalite_rattrapage VARCHAR(255),
     responsable_pedagogique VARCHAR(255),
     intervenants VARCHAR(255),
     type_enseignement VARCHAR(50),
     sequences_rows_json TEXT,
     created_at TIMESTAMP,
     updated_at TIMESTAMP
);


CREATE TABLE course (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_resource_sheet UUID REFERENCES resource_sheet(id),
    sae_id UUID REFERENCES sae(id),
    resource_id UUID REFERENCES resource(id),
    hours INT NOT NULL DEFAULT 0
);


CREATE TABLE list_prof_course (
  id_course UUID REFERENCES course(id) ON DELETE CASCADE,
  id_prof UUID REFERENCES app_user(id) ON DELETE CASCADE,
  PRIMARY KEY (id_course, id_prof)
);

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

CREATE TABLE mccc (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    department TEXT,
    ue TEXT,
    years TEXT,
    semester TEXT,
    form TEXT,
    ressources_rows TEXT,
    saved_at TIMESTAMP DEFAULT now()
);