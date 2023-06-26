package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.File;

import java.util.Optional;

public interface FileDao {
    // =============== Inserts ===============

    public File uploadFile(File file);

    // =============== Queries ===============

    public Optional<File> getFile(long id);
}
