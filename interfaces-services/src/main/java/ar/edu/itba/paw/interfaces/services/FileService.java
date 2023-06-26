package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.File;

import java.util.Optional;

public interface FileService {

    // =============== Inserts ===============

    public File uploadFile(File file);

    // =============== Queries ===============

    public Optional<File> getFile(long id);
}
