Bu Dağıtık Abonelik Sistemi, üç Java sunucusuyla hata toleransı seviyeleri (1 veya 2) ayarlanabilir bir mimariyi kullanarak hata toleranslı bir yapı uygular. Sistem, veri serileştirme için Protocol Buffers'ın verimliliğini kullanır ve iletişim için HASUP protokolünü uygular.

**Bileşenler**

**Protokol Tanımları**

- **Subscriber.proto**: Abone veri yapısını ve abonelik durumlarını (SUBS, ONLN, OFFL) tanımlar.
- **Message.proto**: Sistem komutlarını (STRT, STOP) ve yanıtları (YEP, NOP) tanımlar.
- **Configuration.proto**: Hata tolerans ayarlarını ve sunucu işlemlerini yönetir.
- **Capacity.proto**: Sunucu kapasite izlemesini ve raporlamasını ele alır.

**Sunucu Uygulaması**

Her bir sunucu (Server1.java, Server2.java, Server3.java) şu özellikleri uygular:

- ThreadPool (10 thread) kullanarak eş zamanlı istemci işleme
- ConcurrentHashMap ve ReentrantLock ile thread-safe abone yönetimi
- Üç iletişim kanalı:
  - İstemci iletişimi (portlar 6001-6003)
  - Yönetici iletişimi (portlar 7001-7003)
  - Sunucular arası replikasyon (portlar 5001-5003)

**İstemci Uygulaması (Client.java)**

- Abonelik ve durum yönetimini uygular
- Protocol Buffers kullanarak iletişim sağlar
- Birden fazla bağlantı denemesiyle sunucu yedeklemesini destekler

**Yönetici Paneli (admin.rb)**

- Sunucu kapasitesini izler
- Sistem konfigürasyonunu yönetir
- Şu özellikleri uygular:
  - Konfigürasyon dosyası çözümleme
  - Sunucu başlatma koordinasyonu
  - 5 saniyelik aralıkla kapasite sorgulama
  - Hata tolerans seviyesi yönetimi

**Gerçek Zamanlı İzleme (plotter.py)**

- Sunucu kapasitesini gerçek zamanlı görselleştirir
- Şu özellikleri uygular:
  - Çoklu thread ile veri toplama
  - Gerçek zamanlı grafik güncelleme
  - Sunucuya özgü renk kodlama
  - 5 saniyelik yenileme süresi

**Teknik Kararlar**

**Protocol Buffers Kullanımı**

Tercih edilme sebepleri:

- Verimli serileştirme
- Dil bağımsız iletişim
- Güçlü tip denetimi ve doğrulama
- Geriye dönük uyumluluk

**Eşzamanlılık Yönetimi**

- **ThreadPool**: Kaynakların verimli kullanımı
- **ConcurrentHashMap**: Thread-safe abone depolama
- **ReentrantLock**: Kritik bölge yönetimi

**Hata Toleransı**

Seviyeler:

- **Seviye 1**: Tek sunucu hatasına dayanıklılık
- **Seviye 2**: Çift sunucu hatasına dayanıklılık

Uygulama:

- Aktif veri replikasyonu
- Sunucu sağlığını izleme
- Otomatik yedekleme

**Sunucular Arası İletişim**

- Tam mesh topolojisi
- Güvenilirlik için TCP bağlantıları
- Çift yönlü replikasyon

**Performans Özellikleri**

- Eş zamanlı istemci kapasitesi: Sunucu başına 10 eş zamanlı bağlantı
- Replikasyon gecikmesi: < 100 ms
- Yönetici izleme aralığı: 5 saniye
- Görselleştirme yenileme süresi: 5 saniye

**Güvenlik Hususları**

- Sunucular arası iletişim için ayrı portlar
- Thread-safe veri yapıları
- Protocol Buffers ile girdi doğrulama
- Ayrı istemci ve yönetici arayüzleri

**Gelecek Geliştirmeler**

- SSL/TLS ile geliştirilmiş güvenlik
- Dinamik thread pool boyutlandırması
- Yük dengeleme uygulaması
- Veritabanı kalıcılığı
- Farklı dillerde istemci kütüphaneleri

**Bağımlılıklar**

- Java 11 veya daha üstü
- Protocol Buffers 3.x
- Ruby 2.7+
- Python 3.8+
- matplotlib kütüphanesi
- Ağ portları: 5001-5003, 6001-6003, 7001-7003

**Dağıtım Gereksinimleri**

- Minimum 3 sunucu
- Tüm bileşenler arasında ağ bağlantısı
- Abone verileri için yeterli bellek
- Kullanılabilir ağ portları

**Ekip Üyeleri**

- 19060412-Mehmet Emin SARIKAYA
- 19060429-Samet ŞAMLI
- 19060401-Berkin BAYRİ
