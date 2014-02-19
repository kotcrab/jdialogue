package pl.kotcrab.jdialogue.parser;

public interface CallbackListener
{
	public void handleCallback(String callbackText);
	public boolean handleCallbackCheck(String callbackText);
}
