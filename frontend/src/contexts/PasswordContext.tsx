import React, {
  createContext, useContext, useState, FC, useMemo, PropsWithChildren,
} from 'react';

const LOCAL_STORAGE_PASSWORD_KEY = 'photography_app_admin_password';

type PasswordContextType = {
  password: string;
  setPassword: (_: string) => void;
};

const PasswordContext = createContext<PasswordContextType>({
  password: '',
  setPassword: (_) => 1,
});

export const PasswordContextProvider: FC<PropsWithChildren<{}>> = ({ children }) => {
  const [password, setPassword] = useState<string>(
    window.localStorage.getItem(LOCAL_STORAGE_PASSWORD_KEY) ?? '',
  );
  const passwordContextValue = useMemo(
    () => ({
      password,
      setPassword: (p: string) => {
        window.localStorage.setItem(LOCAL_STORAGE_PASSWORD_KEY, p);
        setPassword(p);
      },
    }),
    [password, setPassword],
  );

  return (
    <PasswordContext.Provider value={passwordContextValue}>
      {children}
    </PasswordContext.Provider>
  );
};

export const usePasswordContext = (): PasswordContextType => useContext(PasswordContext);
