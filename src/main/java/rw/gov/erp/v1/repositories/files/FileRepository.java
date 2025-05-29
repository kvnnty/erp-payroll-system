package rw.gov.erp.v1.repositories.files;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rw.gov.erp.v1.entities.files.StoredFile;

@Repository
public interface FileRepository extends JpaRepository<StoredFile, UUID> {

}
