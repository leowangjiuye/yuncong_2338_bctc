package com.hoho.android.usbserial.listener;

public interface OnReceiveListener {
  void  onMessage(byte[] msg);
}
