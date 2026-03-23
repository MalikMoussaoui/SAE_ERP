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
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(DepartmentRepository departmentRepository,
            UeRepository ueRepository,
            ResourceRepository resourceRepository,
            SaeRepository saeRepository,
            CourseRepository courseRepository,
            AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.departmentRepository = departmentRepository;
        this.ueRepository = ueRepository;
        this.resourceRepository = resourceRepository;
        this.saeRepository = saeRepository;
        this.courseRepository = courseRepository;
        this.appUserRepository = appUserRepository;
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
}