package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.FileDao;
import ar.edu.itba.paw.interfaces.services.FileService;
import ar.edu.itba.paw.models.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {

    private final FileDao fileDao;

    @Autowired
    public FileServiceImpl(FileDao fileDao) {
        this.fileDao = fileDao;
    }


    @Transactional
    @Override
    public File uploadFile(File file) {
        return fileDao.uploadFile(file);
    }

    @Transactional
    @Override
    public Optional<File> getFile(long id) {
        return fileDao.getFile(id);
    }
}
