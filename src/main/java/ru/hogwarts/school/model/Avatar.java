package ru.hogwarts.school.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long avatarId;
    private String filePath;
    private long fileSize;
    private String MediaType;
    @Lob
    private byte[] data;
    @OneToOne
    private Student student;

    public long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(long id) {
        this.avatarId = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
