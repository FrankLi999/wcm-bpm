import React, { ButtonHTMLAttributes } from "react";
import styles from "./Button.module.scss";

type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement>;

const Button: React.FC<ButtonProps> = ({ disabled = false, children }: ButtonProps) => {
  return (
    <button disabled={disabled} className={styles.button}>
      {children}
    </button>
  );
};

export default Button;
