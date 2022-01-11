/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
// Declare any non-default types here with import statements

package com.daftrucks.dafbtcanreceiver.setvinapi;

public interface ISetVinInterface extends android.os.IInterface
{
  /** Default implementation for ISetVinInterface. */
  public static class Default implements ISetVinInterface
  {
    /**
         * Set a new VIN.
         *
         * It is not a problem to pass the same VIN several times. The current status will be returned.
         *
         * @param newVin The new value of the VIN. To clear the VIN, pass an empty string.
         * @return int The status of the VIN.
         */
    @Override public int setVin(String newVin) throws android.os.RemoteException
    {
      return 0;
    }
    /** Get the current, accepted, VIN. */
    @Override public String getVin() throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements ISetVinInterface
  {
    private static final String DESCRIPTOR = "com.daftrucks.dafbtcanreceiver.setvinapi.ISetVinInterface";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.daftrucks.dafbtcanreceiver.setvinapi.ISetVinInterface interface,
     * generating a proxy if needed.
     */
    public static ISetVinInterface asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof ISetVinInterface))) {
        return ((ISetVinInterface)iin);
      }
      return new Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_setVin:
        {
          data.enforceInterface(descriptor);
          String _arg0;
          _arg0 = data.readString();
          int _result = this.setVin(_arg0);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_getVin:
        {
          data.enforceInterface(descriptor);
          String _result = this.getVin();
          reply.writeNoException();
          reply.writeString(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements ISetVinInterface
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      /**
           * Set a new VIN.
           *
           * It is not a problem to pass the same VIN several times. The current status will be returned.
           *
           * @param newVin The new value of the VIN. To clear the VIN, pass an empty string.
           * @return int The status of the VIN.
           */
      @Override public int setVin(String newVin) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(newVin);
          boolean _status = mRemote.transact(Stub.TRANSACTION_setVin, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().setVin(newVin);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      /** Get the current, accepted, VIN. */
      @Override public String getVin() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        String _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getVin, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getVin();
          }
          _reply.readException();
          _result = _reply.readString();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static ISetVinInterface sDefaultImpl;
    }
    static final int TRANSACTION_setVin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_getVin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    public static boolean setDefaultImpl(ISetVinInterface impl) {
      // Only one user of this interface can use this function
      // at a time. This is a heuristic to detect if two different
      // users in the same process use this function.
      if (Proxy.sDefaultImpl != null) {
        throw new IllegalStateException("setDefaultImpl() called twice");
      }
      if (impl != null) {
        Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static ISetVinInterface getDefaultImpl() {
      return Proxy.sDefaultImpl;
    }
  }
  public static final int STATUS_UNKNOWN = -1;
  public static final int STATUS_INVALID = 0;
  public static final int STATUS_VALID = 1;
  public static final int STATUS_VALIDATING = 2;
  /**
       * Set a new VIN.
       *
       * It is not a problem to pass the same VIN several times. The current status will be returned.
       *
       * @param newVin The new value of the VIN. To clear the VIN, pass an empty string.
       * @return int The status of the VIN.
       */
  public int setVin(String newVin) throws android.os.RemoteException;
  /** Get the current, accepted, VIN. */
  public String getVin() throws android.os.RemoteException;
}
