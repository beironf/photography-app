import { Visibility, VisibilityOff } from '@mui/icons-material';
import {
  Button,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  FormControl,
  IconButton,
  InputAdornment,
  InputLabel,
  OutlinedInput,
  TextField,
} from '@mui/material';
import { PhotoApi } from 'api/PhotoApi';
import { usePasswordContext } from 'contexts/PasswordContext';
import { usePromise } from 'hooks';
import React, {
  FC,
  PropsWithChildren,
  useCallback,
  useEffect,
  useState,
} from 'react';
import { useNavigate } from 'react-router-dom';

type Props = {
  hideChildrenIfBlocked?: boolean;
};

export const PasswordRestricted: FC<PropsWithChildren<Props>> = ({
  children,
  hideChildrenIfBlocked,
}) => {
  const [showPassword, setShowPassword] = useState<boolean>(false);
  const [hasValidatedPassword, setHasValidatedPassword] =
    useState<boolean>(false);
  const { password, setPassword } = usePasswordContext();
  const [passwordInput, setPasswordInput] = useState<string>('');
  const [invalidPassword, setInvalidPassword] = useState<string>();
  const navigate = useNavigate();

  const onValidPassword = useCallback(
    () => setPassword(passwordInput),
    [passwordInput, setPassword],
  );
  const onInvalidPassword = useCallback(() => {
    if (password !== '') setPassword('');
    setInvalidPassword(passwordInput);
  }, [password, passwordInput, setPassword, setInvalidPassword]);
  const validatePassword = useCallback(
    () =>
      PhotoApi.validatePassword(
        passwordInput === '' ? password : passwordInput,
        onValidPassword,
        onInvalidPassword,
      ),
    [passwordInput, password, onValidPassword, onInvalidPassword],
  );
  const {
    trigger: triggerValidation,
    data: isValid,
    loading,
  } = usePromise(validatePassword);

  // Update passwordInput with password context
  useEffect(() => {
    if (password !== '') setPasswordInput(password);
  }, [password, setPasswordInput]);

  // Check if pre-stored password (from local storage) is valid
  useEffect(() => {
    if (!hasValidatedPassword && password !== '') triggerValidation();
  }, [password, hasValidatedPassword, triggerValidation]);

  // Know if we have validated anything yet
  useEffect(() => {
    if (isValid !== undefined) setHasValidatedPassword(true);
  }, [isValid]);

  return (
    <>
      <Dialog open={isValid === undefined || !isValid} onClose={undefined}>
        <DialogTitle>Password Required</DialogTitle>
        <DialogContent>
          {!loading && (
            <>
              <DialogContentText>
                This part of the website is intended for admins and is password
                protected. Please provide a password to continue.
              </DialogContentText>
              <form id="password_form" onSubmit={triggerValidation}>
                <TextField id="username-input" autoComplete="username" hidden />
                <FormControl fullWidth sx={{ m: 1 }} variant="outlined">
                  <InputLabel htmlFor="outlined-adornment-password">
                    Password
                  </InputLabel>
                  <OutlinedInput
                    autoFocus
                    autoComplete="current-password"
                    id="password-input"
                    value={passwordInput}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) =>
                      setPasswordInput(e.target.value)
                    }
                    error={
                      hasValidatedPassword &&
                      !isValid &&
                      passwordInput === invalidPassword
                    }
                    type={showPassword ? 'text' : 'password'}
                    endAdornment={
                      <InputAdornment position="end">
                        <IconButton
                          aria-label="toggle password visibility"
                          onClick={() => setShowPassword(!showPassword)}
                          edge="end"
                        >
                          {showPassword ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                      </InputAdornment>
                    }
                    label="Password"
                  />
                </FormControl>
              </form>
            </>
          )}
          {loading && <CircularProgress />}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => navigate('/')}>Return to home</Button>
          <Button
            type="submit"
            form="password_form"
            disabled={
              loading ||
              passwordInput === '' ||
              passwordInput === invalidPassword
            }
            onClick={() => triggerValidation()}
          >
            Validate
          </Button>
        </DialogActions>
      </Dialog>
      {(hideChildrenIfBlocked ? isValid : true) && children}
    </>
  );
};
