package net.schwehla.matrosdms.rcp.swt.popupshell.effects;

/**
 * Thrown when you don't provide all required (or invalid) arguments to an EffectBuilder.
 */
public class InvalidEffectArgumentException extends Exception {
  public InvalidEffectArgumentException(String message) {
    super(message);
  }
}