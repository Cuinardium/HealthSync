import React, {useCallback, useState} from "react";
import { useVacations, useDeleteVacation } from "../../hooks/vacationHooks";
import VacationCard from "./VacationCard";
import {Alert, Button, Modal, Spinner, Stack} from "react-bootstrap";
import { useTranslation } from "react-i18next";
import VacationCardPlaceholder from "./VacationCardPlaceholder";

interface VacationPageProps {
  doctorId: string;
  pageSize: number;
}

const VacationList: React.FC<VacationPageProps> = ({ doctorId, pageSize }) => {
  const {
    data: vacations,
    isLoading,
    error,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useVacations(doctorId, pageSize);

  const { t } = useTranslation();

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [vacationIdToDelete, setVacationIdToDelete] = useState<string | null>(null);


  const deleteVacationMutation = useDeleteVacation(doctorId);

  const handleDeleteVacation = (vacationId: string) => {
    setVacationIdToDelete(vacationId);
    setShowDeleteModal(true);
  };

  const selected = useCallback((id: string) => {
    return id === vacationIdToDelete;
  }, [vacationIdToDelete]);

  if (isLoading) {
    return <Stack direction="vertical" gap={3}>
      {[...Array(pageSize)].map((_, index) => (
        <VacationCardPlaceholder key={index} />
      ))}
    </Stack>;
  }

  if (error) {
    return (
      <div
        className={"text-center d-flex flex-row justify-content-center mt-5"}
      >
        <Alert variant="danger">{t("vacation.errorLoading")}</Alert>
      </div>
    );
  }

  if (
    !vacations ||
    vacations.pages.length === 0 ||
    vacations.pages[0].content.length === 0
  ) {
    return (
      <div
        className={"text-center d-flex flex-row justify-content-center mt-5"}
      >
        <Alert variant="info">{t("vacation.noVacations")}</Alert>
      </div>
    );
  }

  return (
    <div>
      {/* List of vacations */}
      <Stack direction="vertical" gap={3}>
        {vacations.pages.map((page, index) => (
          <React.Fragment key={index}>
            {page.content.map((vacation) => (
              <VacationCard
                key={vacation.id}
                vacation={vacation}
                onDelete={handleDeleteVacation}
                selected={selected(vacation.id)}
              />
            ))}
          </React.Fragment>
        ))}
      </Stack>

      {/* Load more */}
      <div style={{ marginTop: "20px", textAlign: "center" }}>
        {hasNextPage && (
          <Button
            variant="outline-primary"
            onClick={() => fetchNextPage()}
            disabled={isFetchingNextPage}
          >
            {isFetchingNextPage ? (
              <>
                <Spinner
                  as="span"
                  animation="border"
                  size="sm"
                  role="status"
                  aria-hidden="true"
                />{" "}
                {t("vacation.loadingMore")}
              </>
            ) : (
              t("vacation.loadMore")
            )}
          </Button>
        )}
      </div>

        {/* Delete modal */}
      <Modal
        show={showDeleteModal}
        onHide={() => {setShowDeleteModal(false); setVacationIdToDelete(null)}}
        backdrop="static"
        keyboard={false}
        >
        <Modal.Header>
          <Modal.Title>{t("vacation.deleteTitle")}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="text-muted">{t("vacation.deleteDescription")}</div>
        </Modal.Body>
        <Modal.Footer>
            <Button
                variant="secondary"
                onClick={() => {setShowDeleteModal(false); setVacationIdToDelete(null)}}
            >
                {t("vacation.deleteDeny")}
            </Button>
            <Button
                variant="danger"
                onClick={() => {
                deleteVacationMutation.mutate(vacationIdToDelete as string);
                setShowDeleteModal(false);
                setVacationIdToDelete(null);
                }}
                disabled={deleteVacationMutation.isPending}
            >
                {deleteVacationMutation.isPending ? (
                <>
                    <Spinner
                    as="span"
                    animation="border"
                    size="sm"
                    role="status"
                    aria-hidden="true"
                    />
                </>
                ) : (
                t("vacation.deleteConfirm")
                )}
            </Button>
        </Modal.Footer>
        </Modal>
    </div>
  );
};

export default VacationList;
