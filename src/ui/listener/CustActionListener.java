package ui.listener;

import java.awt.event.*;
import java.sql.Connection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.sun.org.apache.bcel.internal.generic.FMUL;

import object.*;

import system.*;
import ui.*;

public class CustActionListener implements ActionListener {

	public final static int LOGIN = 0, FORM_TRANSAKSI_ADDBARANG = 1,
			FORM_TRANSAKSI_SUBMIT = 2, FORM_TRANSAKSI_SELECTITEM = 3,
			LOGOUT = 4, SHOW_DATA_BARANG = 5, SHOW_DATA_TRANSAKSI = 6,
			CETAK_BARANG = 7, HAPUS_BARANG = 8, TAMBAH_BARANG = 9,
			HAPUS_TRANS = 10, FORM_PRODUK_SELECTITEM = 11;
	private Core core;

	private JFrame jf;
	private WindowLogin frmLogin;
	private WindowReport frmReport;
	private WindowFormBarang frmFormBarang;
	private WindowFormTransaksi frmFormTrans;
	private WindowDataTransaksi frmDataTrans;
	private WindowDataBarang frmDataBarang;

	private JButton btn;
	private JComboBox cb, cbSupplier;
	private JMenuItem mi;
	private JTable tbl;

	private JTextField tf;

	private int mode;

	public CustActionListener(Core core, WindowLogin frm, JButton btn) {
		this.core = core;
		this.frmLogin = frm;
		this.btn = btn;
		mode = LOGIN;
	}

	public CustActionListener(Core core, WindowFormTransaksi frm, JButton btn) {
		this.core = core;
		this.frmFormTrans = frm;
		this.btn = btn;
		this.mode = FORM_TRANSAKSI_SUBMIT;
	}

	public CustActionListener(WindowFormTransaksi frm, JButton btn) {
		this.frmFormTrans = frm;
		this.btn = btn;
		this.mode = FORM_TRANSAKSI_ADDBARANG;
	}

	public CustActionListener(WindowFormTransaksi frm, JComboBox cb) {
		this.frmFormTrans = frm;
		this.cb = cb;
		mode = FORM_TRANSAKSI_SELECTITEM;
	}
	
	public CustActionListener(WindowFormBarang frm, JComboBox cbSupplier) {
		this.frmFormBarang = frm;
		this.cbSupplier = cbSupplier;
		mode = FORM_PRODUK_SELECTITEM;
	}

	public CustActionListener(Core core, JFrame jf, JMenuItem mi, int mode) {
		this.core = core;
		this.jf = jf;
		this.mi = mi;
		this.mode = mode;
	}

	public CustActionListener(Core core, WindowFormBarang frm, JMenuItem mi,
			int mode) {
		this.core = core;
		this.frmFormBarang = frm;
		this.mi = mi;
		this.mode = mode;
	}

	public CustActionListener(Core core, WindowFormBarang frm, JTable tbl,JButton btn,
			int mode) {
		this.core = core;
		this.frmFormBarang = frm;
		this.btn = btn;
		this.tbl = tbl;
		this.mode = mode;
	}

	public CustActionListener(Core core, WindowDataTransaksi frm, JTable tbl,
			JButton btn, int mode) 
	{
		this.core = core;
		this.tbl = tbl;
		this.frmDataTrans = frm;
		this.btn = btn;
		this.mode = mode;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (mode) {
		case LOGIN:
			User user = Operator.checkLogin(new User(frmLogin.getUser(),frmLogin.getPass(), false), core.getConnection());
			if (user == null) {
				JOptionPane.showMessageDialog(frmLogin,"Username atau password Salah/nUlangi Lagi");
			} else {
				frmLogin.setVisible(false);
				core.login(user);
			}
			break;
		case FORM_TRANSAKSI_SELECTITEM:
			int index = cb.getSelectedIndex();
			frmFormTrans.fillFormByIndex(index);
			break;
		case FORM_TRANSAKSI_ADDBARANG:
			frmFormTrans.addBarangToTable(new DetilTransaksi(frmFormTrans.getSelectedBarang(), frmFormTrans.getQtyBarang()));
			break;
		case FORM_TRANSAKSI_SUBMIT:
			frmFormTrans.submitToDB();
			break;
		case LOGOUT:
			core.logout();
			break;
		case SHOW_DATA_BARANG:
			if (core.frmDataBarang == null) {
			} else {
				core.frmDataBarang.dispose();
			}
			core.frmDataBarang = new WindowDataBarang(core);
			core.frmDataBarang.setVisible(true);
			break;
		case SHOW_DATA_TRANSAKSI:
			if (core.frmDataTrans == null) {
			} else {
				core.frmDataTrans.dispose();
			}
			core.frmDataTrans = new WindowDataTransaksi(core);
			core.frmDataTrans.setVisible(true);
			break;
		case CETAK_BARANG:
			core.printReport(frmFormBarang.getListBarang());
			break;
		case TAMBAH_BARANG:
			frmFormBarang.submitToDB();
			
			break;
		case HAPUS_BARANG:
			if(tbl == null){
			}else{
				if (Operator.hapusBarang(frmFormBarang.getSelectedBarang(),core.getConnection())) {JOptionPane.showMessageDialog(frmFormBarang,"Data barang dihapus");
				}
				frmFormBarang.resetForm();
			}
			break;
		case HAPUS_TRANS:
			if(tbl == null){
			}else{
				if (Operator.hapusTransaksi(frmDataTrans.getTransaksi(),core.getConnection())) {JOptionPane.showMessageDialog(frmDataTrans,"Data transaksi dihapus");
				}
				frmDataTrans.resetForm();
			}
			break;
		case FORM_PRODUK_SELECTITEM:
			int index1 = cbSupplier.getSelectedIndex();
			frmFormBarang.fillFormByIndex(index1);
			break;
		}

	}
}
