package service;

import domain.Assignment;
import domain.Grade;
import domain.GradeDTO;
import domain.Student;
import org.junit.Before;
import org.junit.Test;
import repo.GradeXMLFileRepo;
import validator.GradeValidator;

import java.io.File;
import java.util.Collection;
import java.util.Objects;

import static org.junit.Assert.*;

public class GradeServiceTest {

    private GradeService service;
    @Before
    public void setUp() {
        String gradeFilePath = "src/test/resources/gradeTest.xml";
        service = new GradeService(new GradeXMLFileRepo(new GradeValidator(),
                gradeFilePath));
        GradeService.storeDirectoryPath = "src/test/resources/Students/";
        File studentsDir = new File(GradeService.storeDirectoryPath);
        boolean deleted = true;
        if(studentsDir.isDirectory()){
            for (String fileName : Objects.requireNonNull(studentsDir.list())) {
                File file = new File(GradeService.storeDirectoryPath + fileName);
                if(file.isFile() && file.length() > 100) {
                    deleted = file.delete() && deleted;
                }
            }
            if(!deleted){
                System.out.println("Could not delete all files in: " +
                        GradeService.storeDirectoryPath);
            }
        }
    }

    @Test
    public void getCurrentWeek() {
        assertTrue(GradeService.getCurrentWeek() > 8);
    }

    @Test
    public void getGrade() {
        Grade entity = new Grade();
        entity.setID("11");
        assertEquals(service.getGrade(entity).toString(),
                "1 Ion 225 ion@ion.com Albert 1 Tema1 5 3 4 7.5");
        entity.setID("100");
        assertNull(service.getGrade(entity));
        entity.setID("11");
        entity = service.getGrade(entity);
        GradeDTO entityDTO = GradeDTO.toGradeDTO(entity);
        assertNotNull(service.getGrade(entityDTO));
    }

    @Test
    public void addStudentGrade() {
        Student student = new Student("1", "Ion", 225,
                "ion@ion.com", "Albert");
        Assignment assignment = new Assignment("1", "Tema1", 5, 3);
        Grade grade = new Grade(student, assignment, 4, 10f);
        grade = service.getGrade(grade);

        assertNotNull(grade);

        boolean response = service.addStudentGrade(grade, "Te-ai descurcat bine.");
        assertTrue(response);
        response = service.addStudentGrade(grade, "Se putea si mai bine.");
        assertTrue(response);
    }

    @Test
    public void addGrade() {
        Student student = new Student("4", "Ionut", 223,
                "ionut@ion.com", "Maria");
        Assignment assignment = new Assignment("5", "tema5", 5, 3);
        Grade grade = new Grade(student, assignment, 6, 10f);

        Grade response = service.addGrade(grade, false);
        assertNull(response);

        response = service.addGrade(grade, false);
        assertNotNull(response);
    }

    @Test
    public void deleteGrade(){
        Student student = new Student("4", "Ionut", 223,
                "ionut@ion.com", "Maria");
        Assignment assignment = new Assignment("5", "tema5", 5, 3);
        Grade grade = new Grade(student, assignment,6,10f);

        Grade response = service.deleteGrade(grade);
        assertNotNull(response);

        response = service.deleteGrade(grade);
        assertNull(response);
    }
    @Test
    public void filterBy() {
        Collection<Grade> passingGrades = service.filterBy((grade -> grade.getGrade() > 5f));
        passingGrades.forEach(grade -> assertTrue(grade.getGrade() > 5f));
    }
}