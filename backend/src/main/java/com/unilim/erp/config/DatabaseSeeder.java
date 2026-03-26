package com.unilim.erp.config;

import com.unilim.erp.domain.UserRole;
import com.unilim.erp.domain.UserStatus;
import com.unilim.erp.entities.*;
import com.unilim.erp.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final UeRepository ueRepository;
    private final ResourceRepository resourceRepository;
    private final SaeRepository saeRepository;
    private final CourseRepository courseRepository;
    private final AppUserRepository appUserRepository;
    private final ResourceSheetRepository resourceSheetRepository;
    private final McccRepository mcccRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(DepartmentRepository departmentRepository,
            UeRepository ueRepository,
            ResourceRepository resourceRepository,
            SaeRepository saeRepository,
            CourseRepository courseRepository,
            AppUserRepository appUserRepository,
            ResourceSheetRepository resourceSheetRepository,
            McccRepository mcccRepository,
            PasswordEncoder passwordEncoder) {
        this.departmentRepository = departmentRepository;
        this.ueRepository = ueRepository;
        this.resourceRepository = resourceRepository;
        this.saeRepository = saeRepository;
        this.courseRepository = courseRepository;
        this.appUserRepository = appUserRepository;
        this.resourceSheetRepository = resourceSheetRepository;
        this.mcccRepository = mcccRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Vérification de l'existence de l'administrateur...");
        appUserRepository.findByEmail("admin@unilim.fr").ifPresentOrElse(
                user -> {
                    user.setPasswordHash(passwordEncoder.encode("1234"));
                    user.setRole(UserRole.ADMINISTRATEUR);
                    appUserRepository.save(user);
                    log.info("Mot de passe et rôle de l'admin forcés.");
                },
                () -> {
                    log.info("Création de l'admin car il n'existe pas.");
                    AppUser admin = createUser("System", "Admin", "admin@unilim.fr", UserRole.ADMINISTRATEUR,
                            UserStatus.ACTIVE, null);
                    appUserRepository.save(admin);
                });

        Department deptInfo = departmentRepository.findAll().stream()
                .filter(d -> "INFO".equals(d.getLabel()))
                .findFirst()
                .orElseGet(() -> {
                    Department d = new Department();
                    d.setLabel("INFO");
                    return departmentRepository.save(d);
                });

        appUserRepository.findByEmail("direction@unilim.fr").ifPresentOrElse(
                user -> log.info("Direction existe déjà."),
                () -> {
                    AppUser dir = createUser("Mourad", "Khalid", "direction@unilim.fr", UserRole.DIRECTION,
                            UserStatus.ACTIVE, deptInfo);
                    appUserRepository.save(dir);
                    log.info("Création du compte Direction: direction@unilim.fr");
                });

        appUserRepository.findByEmail("rh@unilim.fr").ifPresentOrElse(
                user -> log.info("RH existe déjà."),
                () -> {
                    AppUser rh = createUser("Ressources", "Humaines", "rh@unilim.fr", UserRole.RH, UserStatus.ACTIVE,
                            null);
                    appUserRepository.save(rh);
                    log.info("Création du compte RH: rh@unilim.fr");
                });

        // Migration: Update existing users that have no department
        appUserRepository.findAll().forEach(u -> {
            if (u.getDepartment() == null &&
                    u.getRole() != UserRole.ADMINISTRATEUR &&
                    u.getRole() != UserRole.RH) {
                u.setDepartment(deptInfo);
                appUserRepository.save(u);
                log.info("Mis à jour du département pour l'utilisateur existant: " + u.getEmail());
            }
        });
        cleanupLegacyDepartments();
        seedAllDepartmentsAndUes();

        if (appUserRepository.count() > 10) {
            return;
        }

        log.info("Seeding : Création du personnel IUT (Profs, Vacataires)...");

        Department deptGea = departmentRepository.findAll().stream()
                .filter(d -> "GEA".equals(d.getLabel()))
                .findFirst()
                .orElseGet(() -> {
                    Department d = new Department();
                    d.setLabel("GEA");
                    return departmentRepository.save(d);
                });

        AppUser respInfo = createUser("Turing", "Alan", "alan.turing@unilim.fr", UserRole.RESPONSABLE_PEDAGOGIQUE,
                UserStatus.ACTIVE, deptInfo);
        AppUser profJava = createUser("Lovelace", "Ada", "ada.lovelace@unilim.fr", UserRole.TEACHER, UserStatus.ACTIVE,
                deptInfo);
        AppUser profWeb = createUser("Berners-Lee", "Tim", "tim.berners@unilim.fr", UserRole.TEACHER, UserStatus.ACTIVE,
                deptInfo);
        AppUser referentBdd = createUser("Codd", "Edgar", "edgar.codd@unilim.fr", UserRole.REFERENT, UserStatus.ACTIVE,
                deptInfo);
        AppUser vacataire = createUser("Musk", "Elon", "elon.musk@tesla.com", UserRole.VACATAIRE, UserStatus.ACTIVE,
                deptInfo);
        AppUser profSuspendu = createUser("Dalton", "Joe", "joe.dalton@unilim.fr", UserRole.TEACHER,
                UserStatus.SUSPENDED, deptInfo);
        AppUser rhSophie = createUser("Durand", "Sophie", "sophie.durand@unilim.fr", UserRole.RH, UserStatus.ACTIVE,
                null);

        for (AppUser u : Arrays.asList(respInfo, profJava, profWeb, referentBdd, vacataire, profSuspendu, rhSophie)) {
            if (appUserRepository.findByEmail(u.getEmail()).isEmpty()) {
                appUserRepository.save(u);
            }
        }

        if (ueRepository.count() == 0) {
            Ue ueDev = createUE("UE 1.1 - Développement", 1, deptInfo);
            Ue ueDonnees = createUE("UE 1.2 - Données", 1, deptInfo);
            ueRepository.saveAll(Arrays.asList(ueDev, ueDonnees));

            Resource rJava = createResource("R1.01 - Java", 10, 20, 30, ueDev);
            Resource rWeb = createResource("R1.02 - Web", 10, 15, 15, ueDev);
            Resource rBdd = createResource("R1.05 - SQL & Base de données", 12, 14, 20, ueDonnees);
            resourceRepository.saveAll(Arrays.asList(rJava, rWeb, rBdd));

            Sae saeSite = new Sae();
            saeSite.setTitle("SAE 1.01 - Site Web E-Commerce");
            saeSite.setHours(40);
            saeRepository.save(saeSite);

            createCourse(2, rJava, null, respInfo);
            createCourse(4, rJava, null, profJava);
            createCourse(4, rWeb, null, profWeb);
            createCourse(2, rBdd, null, referentBdd);

            Course coursProjet = new Course();
            coursProjet.setHours(12);
            coursProjet.setSae(saeSite);
            coursProjet.setTeachers(new HashSet<>());
            coursProjet.getTeachers().add(vacataire);
            courseRepository.save(coursProjet);
        }
        log.info("Seeding terminé : Base prête et sécurisée !");
    }

    private AppUser createUser(String nom, String prenom, String email, UserRole role, UserStatus status,
            Department department) {
        AppUser user = new AppUser();
        user.setDisplayName(prenom + " " + nom);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode("1234"));
        user.setPhone("0601020304");
        user.setRole(role);
        user.setStatus(status);
        user.setDepartment(department);
        return user;
    }

    private Ue createUE(String titre, int semestre, Department dept) {
        Ue ue = new Ue();
        ue.setTitle(titre);
        ue.setSemester(semestre);
        ue.setDepartment(dept);
        return ue;
    }

    private Resource createResource(String titre, int cm, int td, int tp, Ue ue) {
        Resource r = new Resource();
        r.setTitle(titre);
        r.setCmHours(cm);
        r.setTdHours(td);
        r.setTpHours(tp);
        r.setUe(ue);
        return r;
    }

    private void createCourse(int hours, Resource resource, Sae sae, AppUser prof) {
        Course c = new Course();
        c.setHours(hours);
        if (resource != null)
            c.setResource(resource);
        if (sae != null)
            c.setSae(sae);

        if (prof != null) {
            c.setTeachers(new HashSet<>());
            c.getTeachers().add(prof);
        }
        courseRepository.save(c);
    }

    private void seedAllDepartmentsAndUes() {
        // Pour informatique
        Department info = fetchOrCreateDept("INFO");
        String[] infoS1toS4 = { "Réaliser", "Optimiser", "Administrer", "Gérer", "Conduire", "Collaborer" };
        for (int sem = 1; sem <= 4; sem++) {
            for (int i = 0; i < infoS1toS4.length; i++) {
                createUeIfMissing("UE " + sem + "." + (i + 1) + " " + infoS1toS4[i], sem, info);
            }
        }
        String[] infoS5 = { "Réaliser un dev d'application", "Optimiser des applications",
                "Administrer des SI complexes", "Gérer des données", "Conduire un projet", "Travailler en équipe" };
        for (int i = 0; i < infoS5.length; i++) {
            createUeIfMissing("UE 5." + (i + 1) + " " + infoS5[i], 5, info);
        }
        for (int i = 0; i < infoS1toS4.length; i++) {
            createUeIfMissing("UE 6." + (i + 1) + " " + infoS1toS4[i], 6, info);
        }

        // pour MMI
        Department mmi = fetchOrCreateDept("MMI");
        String[] mmiUes = { "Comprendre", "Concevoir", "Exprimer", "Développer", "Entreprendre" };
        seedGenericUes(mmi, mmiUes, 6);

        // pour GEA
        Department gea = fetchOrCreateDept("GEA");
        String[] geaUes = { "Analyser les processus", "Produire l'info comptable", "Gérer les ressources humaines",
                "Piloter les organisations", "Communiquer en contexte pro" };
        seedGenericUes(gea, geaUes, 6);

        // pour GB
        Department gb = fetchOrCreateDept("GB");
        String[] gbUes = { "Analyser et caractériser", "Concevoir et produire", "Contrôler et assurer la qualité",
                "Organiser et gérer", "Communiquer et se former" };
        seedGenericUes(gb, gbUes, 6);

        // pour GMP
        Department gmp = fetchOrCreateDept("GMP");
        String[] gmpUes = { "Analyser un système", "Concevoir un produit/process", "Industrialiser un produit",
                "Gérer une unité de production", "S'intégrer dans une organisation" };
        seedGenericUes(gmp, gmpUes, 6);

        // pour MP
        Department mp = fetchOrCreateDept("MP");
        String[] mpUes = { "Mesurer et caractériser", "Analyser et modéliser", "Concevoir des systèmes de mesure",
                "Traiter et exploiter les données", "Intégrer et évoluer en milieu pro" };
        seedGenericUes(mp, mpUes, 6);

        // pour RT
        Department rt = fetchOrCreateDept("RT");
        String[] rtUes = { "Administrer les réseaux", "Connecter les entreprises", "Créer des outils & services",
                "Sécuriser les infrastructures", "Gérer les données", "Travailler en mode projet" };
        seedGenericUes(rt, rtUes, 6);

        // pour Tech de Co
        Department tc = fetchOrCreateDept("TC");
        String[] tcUes = { "Développer la relation client", "Mettre en oeuvre une stratégie marketing",
                "Vendre une offre commerciale", "Analyser le marché", "Conduire un projet commercial", "Communiquer" };
        seedGenericUes(tc, tcUes, 6);
    }

    private Department fetchOrCreateDept(String label) {
        return departmentRepository.findAll().stream()
                .filter(d -> label.equals(d.getLabel()))
                .findFirst()
                .orElseGet(() -> {
                    Department d = new Department();
                    d.setLabel(label);
                    return departmentRepository.save(d);
                });
    }

    private void seedGenericUes(Department dept, String[] suffixes, int semesters) {
        for (int sem = 1; sem <= semesters; sem++) {
            for (int i = 0; i < suffixes.length; i++) {
                createUeIfMissing("UE " + sem + "." + (i + 1) + " " + suffixes[i], sem, dept);
            }
        }
    }

    private void createUeIfMissing(String title, int semester, Department dept) {
        boolean exists = ueRepository.findAll().stream()
                .anyMatch(u -> u.getTitle().equals(title) && u.getSemester() == semester
                        && u.getDepartment().getId().equals(dept.getId()));
        if (!exists) {
            ueRepository.save(createUE(title, semester, dept));
        }
    }

    private void cleanupLegacyDepartments() {
        Department info = fetchOrCreateDept("INFO");
        departmentRepository.findAll().forEach(d -> {
            if (!d.getId().equals(info.getId())
                    && (d.getLabel().toLowerCase().contains("informatique") || d.getLabel().equalsIgnoreCase("INFO"))) {
                mergeDepartment(d, info);
            }
        });

        Department gea = fetchOrCreateDept("GEA");
        departmentRepository.findAll().forEach(d -> {
            if (!d.getId().equals(gea.getId())
                    && (d.getLabel().toLowerCase().contains("gestion") || d.getLabel().equalsIgnoreCase("GEA"))) {
                mergeDepartment(d, gea);
            }
        });

        Department mmi = fetchOrCreateDept("MMI");
        departmentRepository.findAll().forEach(d -> {
            if (!d.getId().equals(mmi.getId())
                    && (d.getLabel().toLowerCase().contains("multimedia") || d.getLabel().equalsIgnoreCase("MMI"))) {
                mergeDepartment(d, mmi);
            }
        });
    }

    private void mergeDepartment(Department oldDept, Department newDept) {
        appUserRepository.findAll().forEach(u -> {
            if (u.getDepartment() != null && u.getDepartment().getId().equals(oldDept.getId())) {
                u.setDepartment(newDept);
                appUserRepository.save(u);
            }
        });
        resourceSheetRepository.findAll().forEach(rs -> {
            if (rs.getDepartment() != null && rs.getDepartment().getId().equals(oldDept.getId())) {
                rs.setDepartment(newDept);
                resourceSheetRepository.save(rs);
            }
        });
        mcccRepository.findAll().forEach(m -> {
            if (m.getDepartment() != null && m.getDepartment().getId().equals(oldDept.getId())) {
                m.setDepartment(newDept);
                mcccRepository.save(m);
            }
        });
        ueRepository.findAll().forEach(ue -> {
            if (ue.getDepartment() != null && ue.getDepartment().getId().equals(oldDept.getId())) {
                ue.setDepartment(newDept);
                ueRepository.save(ue);
            }
        });
        departmentRepository.delete(oldDept);
        log.info("Merged duplicate department '{}' into '{}'", oldDept.getLabel(), newDept.getLabel());
    }
}