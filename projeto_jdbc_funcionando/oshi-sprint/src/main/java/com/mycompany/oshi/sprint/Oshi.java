/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.oshi.sprint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.Baseboard;
import oshi.hardware.CentralProcessor;

import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.ComputerSystem;
import oshi.hardware.Display;
import oshi.hardware.Firmware;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.hardware.PowerSource;
import oshi.hardware.Sensors;
import oshi.hardware.UsbDevice;
import oshi.software.os.FileSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.util.FormatUtil;
import oshi.util.Util;

/**
 *
 * @author Aluno
 */
public class Oshi {

    SystemInfo si = new SystemInfo();

    HardwareAbstractionLayer hal = si.getHardware();
    OperatingSystem os = si.getOperatingSystem();

    private String sistemaOperacional = os.toString();

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    private String fabricantePc = hal.getComputerSystem().getManufacturer();

    public String getFabricantePc() {
        return fabricantePc;
    }

    private String modeloPc = hal.getComputerSystem().getModel();

    public String getModeloPc() {
        return modeloPc;
    }

    private String numeroDeSeriePc = hal.getComputerSystem().getSerialNumber();

    public String getNumeroDeSeriePc() {
        return numeroDeSeriePc;
    }

    private String processadorPc = hal.getProcessor().getIdentifier();

    public String getProcessadorPc() {
        return processadorPc;
    }

    private Integer memoriaUtilizada = printMemory(hal.getMemory());

    private static Integer printMemory(GlobalMemory memory) {

        Long memoriaDispo = memory.getAvailable();
        Long memoriaTotal = memory.getTotal();
        Long memoriaPorcent = 100 - ((memoriaDispo * 100) / memoriaTotal);

        return Integer.parseInt(memoriaPorcent.toString());
    }

    public Integer getMemoriaUtilizada() {
        return memoriaUtilizada;
    }

    private Integer cpuUtilizada = printCpu(hal.getProcessor());

    public Integer printCpu(CentralProcessor processor) {

        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        long sys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        long totalCpu = user + nice + sys + idle + iowait + irq + softirq;
        Double resultado = (100d * user / totalCpu);
        String total = String.format("%.0f", resultado);
        return Integer.valueOf(total);
    }

    public Integer getCpuUtilizada() {
        return cpuUtilizada;
    }

    private String numeroProcessos;

    private List<Integer> idProcesso = new ArrayList<>();

    private List<String> cpuUsadaProce = new ArrayList<>();

    private List<String> memoriaUsadaProce = new ArrayList<>();

    private List<String> vszProce = new ArrayList<>();

    private List<String> rssProce = new ArrayList<>();

    private List<String> nomeProce = new ArrayList<>();

    Boolean pegarProcesso = printProcesses();
    
    private Boolean printProcesses() {

        OperatingSystem so = si.getOperatingSystem();
        
        GlobalMemory memory = hal.getMemory();
        
        List<OSProcess> procs = Arrays.asList(so.getProcesses(10, ProcessSort.CPU));

        numeroProcessos = ("Processos: " + so.getProcessCount()
                + ", Threads: " + so.getThreadCount());

        for (int i = 0; i < procs.size() && i < 10; i++) {
            OSProcess p = procs.get(i);
            
            idProcesso.add(p.getProcessID());

            Double resultadoCpu = (100d * (p.getKernelTime() + p.getUserTime())
                    / p.getUpTime()); 
            String cpuForma = String.format("%.3f", resultadoCpu);
            cpuUsadaProce.add(cpuForma);

            Double resultadoMemoria = 100d * p.getResidentSetSize()
                    / memory.getTotal();
            String memoriaForma = String.format("%.3f", resultadoMemoria);
            memoriaUsadaProce.add(memoriaForma);

            vszProce.add(FormatUtil.formatBytes(p.getVirtualSize()));

            rssProce.add(FormatUtil.formatBytes(p.getResidentSetSize()));
            
            nomeProce.add(p.getName());
        }
        
        return true;
    }

    public String getNumeroProcessos() {
        return numeroProcessos;
    }

    public List<Integer> getIdProcesso() {
        return idProcesso;
    }

    public List<String> getCpuUsadaProce() {
        return cpuUsadaProce;
    }

    public List<String> getMemoriaUsadaProce() {
        return memoriaUsadaProce;
    }

    public List<String> getVszProce() {
        return vszProce;
    }

    public List<String> getRssProce() {
        return rssProce;
    }

    public List<String> getNomeProce() {
        return nomeProce;
    }
}
