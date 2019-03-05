package utils;

import domain.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DataConvert {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * given an array of bytes, returns a string
     * containing hexadecimal characters
     * 1 character for the upper 4 bits and 1 for the lower ones
     * @param bytes array of bytes, each 4 bits defines a string character
     * @return string containing hexadecimal characters
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * create a student from a comma separated line
     * @param line fields delimited by ","
     * @return entity if entity could be made
     *         null - otherwise
     */
    public static Student studentFromCSV(String line){
        String[] fields = line.split(",");
        for(int i = 0; i < fields.length; ++i){
            fields[i] = fields[i].trim();
        }
        try {
            Student student = new Student(fields[0], Integer.parseInt(fields[1]), fields[2], fields[3]);
            Student.assignId(student);
            return student;
        } catch (NumberFormatException ex){
            return null;
        }
    }

    /**
     * returns a list of entities from the data stored in a file
     * @param filePath the path where the dataFile exists
     * @return list of entities
     */
    public static List<Student> loadStudentsDataFromCSV(File filePath){
        List<Student> students = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath.toPath());
            lines.removeIf(String::isEmpty);
            lines.sort(String::compareTo);
            //Index.resetIndex();
            lines.forEach(
                    line -> {
                        Student stud = studentFromCSV(line);
                        if(stud != null)
                        students.add(studentFromCSV(line));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * return a student from element fields
     * @param element xml element containing fields
     * @return student from fields
     */
    public static Student studentFromElement(Element element){
        String id = element.getAttribute("student_id");
        String name = element.getElementsByTagName("name")
                .item(0).getTextContent();
        Integer group = Integer.parseInt(element.getElementsByTagName("group")
                .item(0).getTextContent());
        String email = element.getElementsByTagName("email")
                .item(0).getTextContent();
        String teacher = element.getElementsByTagName("teacher")
                .item(0).getTextContent();
        long studIndex = Index.getIndexId(id);
        if(studIndex > Index.getIndex()){
            Index.setIndex(studIndex);
        }
        return new Student(id, name, group, email, teacher);
    }

    /**
     * return an assignment from xml element
     * @param element xml element containing fields
     * @return assignment from element fields
     */
    public static Assignment assignmentFromElement(Element element){
        String id = element.getAttribute("assignmentId");
        String description = element.getElementsByTagName("description")
                .item(0).getTextContent();
        Integer deadline = Integer.parseInt(element.getElementsByTagName("deadline")
                .item(0).getTextContent());
        Integer weekReceived =  Integer.parseInt(element.getElementsByTagName("week_received")
                .item(0).getTextContent());

        return new Assignment(id, description, deadline, weekReceived);
    }

    /**
     * return a grade from xml element
     * @param element xml element containing fields
     * @return grade from element fields
     */
    public static Grade gradeFromElement(Element element){
        Element studentElem = (Element) element.getElementsByTagName("student")
                .item(0);
        Element assignmentElem = (Element) element.getElementsByTagName("assignment")
                .item(0);
        Student student = studentFromElement(studentElem);
        Assignment assignment = assignmentFromElement(assignmentElem);
        int weekGraded = Integer.parseInt(element.getElementsByTagName("week_graded")
                .item(0).getTextContent());

        Float grade = Float.parseFloat(element.getElementsByTagName("grade")
                .item(0).getTextContent());

        return new Grade(student, assignment, weekGraded, grade);
    }

    /**
     * return an account from xml element
     * @param element xml element containing fields
     * @return account from element fields
     */
    public static Account accountFromElement(Element element){
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        String password = element.getElementsByTagName("password").item(0).getTextContent();
        String accountType = element.getElementsByTagName("account_type").item(0).getTextContent();
        return new Account(name, password, AccountType.valueOf(accountType));
    }

    /**
     * @param document xml document
     * @param entity entity to make xml element from
     * @return xml element containing entity fields
     */
    public static Element elementFromAccount(Document document, Account entity){
        Element element = document.createElement("account");

        Element name = document.createElement("name");
        name.setTextContent(entity.getAccountName());
        element.appendChild(name);

        Element password = document.createElement("password");
        password.setTextContent(entity.getPassword());
        element.appendChild(password);

        Element accountType = document.createElement("account_type");
        accountType.setTextContent(entity.getAccountType().toString());
        element.appendChild(accountType);

        return element;
    }

    /**
     * @param document xml document
     * @param entity entity to make xml element from
     * @return xml element containing entity fields
     */
    public static Element elementFromStudent(Document document, Student entity){
        Element element = document.createElement("student");
        element.setAttribute("student_id", entity.getID());

        Element name = document.createElement("name");
        name.setTextContent(entity.getName());
        element.appendChild(name);

        Element group = document.createElement("group");
        group.setTextContent(entity.getGroup().toString());
        element.appendChild(group);

        Element email = document.createElement("email");
        email.setTextContent(entity.getEmail());
        element.appendChild(email);

        Element teacher = document.createElement("teacher");
        teacher.setTextContent(entity.getTeacher());
        element.appendChild(teacher);

        return element;
    }

    /**
     * @param document xml document
     * @param entity entity to make xml element from
     * @return xml element containing entity fields
     */
    public static Element elementFromAssignment(Document document, Assignment entity){
        Element element = document.createElement("assignment");
        element.setAttribute("assignmentId", entity.getID());

        Element description = document.createElement("description");
        description.setTextContent(entity.getDescription());
        element.appendChild(description);


        Element deadline = document.createElement("deadline");
        deadline.setTextContent(entity.getDeadline().toString());
        element.appendChild(deadline);

        Element weekReceived = document.createElement("week_received");
        weekReceived.setTextContent(entity.getWeekReceived().toString());
        element.appendChild(weekReceived);

        return element;
    }

    /**
     * @param document xml document
     * @param entity entity to make xml element from
     * @return xml element containing entity fields
     */
    public static Element elementFromGrade(Document document, Grade entity){
        Element element = document.createElement("grade");

        Student student = entity.getStudent();
        Assignment assignment = entity.getAssignment();

        Element studentElem = elementFromStudent(document, student);
        element.appendChild(studentElem);

        Element assignmentElem = elementFromAssignment(document, assignment);
        element.appendChild(assignmentElem);

        Element weekGraded = document.createElement("week_graded");
        weekGraded.setTextContent(entity.getWeekGraded().toString());
        element.appendChild(weekGraded);

        Element grade = document.createElement("grade");
        grade.setTextContent(entity.getGrade().toString());
        element.appendChild(grade);

        return element;
    }
}
