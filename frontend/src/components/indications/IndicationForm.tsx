import React, { useRef, useState } from "react";
import { Button, Form, InputGroup, Spinner, Stack } from "react-bootstrap";
import { useCreateIndication } from "../../hooks/indicationHooks";
import { AxiosError } from "axios";
import { useTranslation } from "react-i18next";
import { FaPaperclip, FaPaperPlane } from "react-icons/fa";
import { IndicationForm as IndicationFormType } from "../../api/indication/Indication";
import { Controller, useForm } from "react-hook-form";
import {
  validateFile,
  validateIndications,
} from "../../api/validation/validations";
import { FaX } from "react-icons/fa6";

interface CreateIndicationFormProps {
  appointmentId: string;
  onSuccess?: () => void;
}

const IndicationForm: React.FC<CreateIndicationFormProps> = ({
  appointmentId,
  onSuccess: onSuccessCallback = () => {},
}) => {
  const { t } = useTranslation();
  const {
    register,
    control,
    handleSubmit,
    setValue,
    setError,
    formState: { errors },
    reset,
    watch,
  } = useForm<IndicationFormType>();
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  const [fileName, setFileName] = useState<string | null>(null);

  const indications = watch("indications");
  const [showError, setShowError] = useState<boolean>(false);
  
  const onSuccess = () => {
    reset();
    setFileName(null);
    setValue("indications", "");
    setShowError(false);
    onSuccessCallback();
  };

  const onError = (error: AxiosError) => {
    setShowError(true);
  };

  const createIndicationMutation = useCreateIndication(
    appointmentId,
    onSuccess,
    onError,
  );

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const valid = validateFile(file);

      if (valid === true) {
        setFileName(file.name);
        setValue("file", file);
      } else {
        setError("file", {
          type: "manual",
          message: valid,
        });
      }
    }
  };

  const deleteFile = () => {
    setFileName(null);
    setValue("file", undefined);
  };

  const triggerFileInput = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const onSubmit = (data: IndicationFormType) => {
    const file = data.file;
    if (!file || !(file instanceof File)) {
      data.file = undefined;
    }

    createIndicationMutation.mutate(data);
  };

  return (
    <Form onSubmit={handleSubmit(onSubmit)}>
      <Stack direction="horizontal" gap={3}>
        <InputGroup>
          {/* Description Input */}
          <Controller
            name="indications"
            control={control}
            defaultValue=""
            rules={{ validate: validateIndications }}
            render={({ field }) => (
              <Form.Control
                {...field}
                as={"textarea"}
                placeholder={t("appointment.addIndication")}
                isInvalid={!!errors.indications}
              />
            )}
          />
          {/* File Trigger */}
          {fileName && (
            <>
              <InputGroup.Text>{fileName}</InputGroup.Text>

              <Button variant="danger" onClick={deleteFile}>
                <FaX />
              </Button>
            </>
          )}
          <Button variant="outline-secondary" onClick={triggerFileInput}>
            <FaPaperclip />
          </Button>
        </InputGroup>

        {/* Hidden File Input */}
        <Form.Control
          type="file"
          accept="image/png, image/jpeg, image/jpg, application/pdf"
          ref={(e: HTMLInputElement | null) => {
            register("file").ref(e);
            fileInputRef.current = e;
          }}
          style={{ display: "none" }}
          onChange={handleFileChange}
        />

        <Button
          variant="primary"
          type="submit"
          disabled={
            createIndicationMutation.isPending ||
            !indications ||
            indications?.trim() === ""
          }
        >
          {createIndicationMutation.isPending ? (
            <Spinner
              as="span"
              animation="border"
              size="sm"
              role="status"
              aria-hidden="true"
            />
          ) : (
            <FaPaperPlane />
          )}
        </Button>
      </Stack>
        
      {/* Error Message */}
      <p className="text-danger mt-2">
        {errors.indications && t(errors.indications.message ?? "")}{" "}
        {errors.file && t(errors.file.message ?? "")}
      </p>

      {showError &&  <p className="text-danger mt-2">
        {t("appointment.errorIndication")}
        </p>
      }

    </Form>
  );
};

export default IndicationForm;
